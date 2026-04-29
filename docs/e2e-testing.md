# E2E Testing

Smoke-level instrumentation tests that drive the real APK against a mocked
Last.fm API. The goal is "does the app still launch, log in, and render core
screens" — anything finer-grained belongs in unit tests or Roborazzi screenshot
tests (see [`testing.md`](testing.md)).

Lives in `app/src/androidTest/`. Runs weekly on CI via
`.github/workflows/e2e_test.yml` (also `workflow_dispatch`).

## Commands

| Action | Command |
|--------|---------|
| Run locally on a connected emulator | `./gradlew :app:connectedDebugAndroidTest` |
| Run locally on the Gradle Managed Device used by CI | `./gradlew :app:pixel6Api35DebugAndroidTest` |
| Build the test APK only | `./gradlew :app:assembleDebugAndroidTest` |

The `pixel6Api35` device is declared in `app/build.gradle.kts` via
`testOptions.managedDevices.allDevices` (Pixel 6, API 35, Google APIs x86_64).
First local run downloads the system image and creates the AVD; subsequent
runs reuse it.

### Avoid overwriting your locally-installed dev app

Without any flag, `connectedDebugAndroidTest` builds the debug APK with
`applicationId = com.mataku.scrobscrob.dev` — the same as `installDebug`,
so the test run reinstalls and overwrites your hand-installed dev build.

Pass `-PforAndroidTest=true` to switch the suffix to `.dev.test` for that
invocation only:

```
./gradlew :app:connectedDebugAndroidTest -PforAndroidTest=true
```

This installs as `com.mataku.scrobscrob.dev.test`, leaving your
`com.mataku.scrobscrob.dev` app untouched. CI does not pass the flag, so
the e2e workflow keeps using the `.dev` suffix.

Side effect: with the suffix changed, no entry in `google-services.json`
matches, so the Google Services plugin would normally fail. `app/build.gradle.kts`
sets `missingGoogleServicesStrategy = WARN` **only when `forAndroidTest=true`
is passed** — normal builds keep the default ERROR strategy so a real
applicationId mismatch still fails the build. Under the test variant,
Firebase features (Crashlytics, etc.) won't initialize — fine for the
mocked-API e2e suite, but don't rely on Firebase runtime behavior here.

## Architecture

The instrumentation test process loads a `TestApp` instead of the production
`App`. `TestApp` builds a `TestAppGraph` that excludes the production
`HttpClientEngine` provider and contributes a `MockEngine` that serves canned
JSON from `assets/`.

```
MetroTestRunner (testInstrumentationRunner)
  └─ newApplication() ─► TestApp
                          └─ newAppGraph() ─► TestAppGraph
                                                ├─ excludes HttpEngineModule
                                                └─ includes MockApiModule (MockEngine)
                                                                    └─ FixtureDispatcher
                                                                          └─ assets/*.json
```

Production-side hooks that make this possible:

- **`AppGraphContract`** (in `app/.../di/AppGraph.kt`) — neutral supertype
  shared by `AppGraph` and `TestAppGraph`. `App.appGraph` is typed against this
  contract so test/prod graphs are interchangeable.
- **`App.newAppGraph()`** is `internal open` — `TestApp` overrides it.
- **`HttpEngineModule`** (in `data/api/.../di/`) is a separate
  `@ContributesTo(AppScope::class)` interface whose only job is to provide
  `HttpClientEngine`. Keeping it in its own binding container is what lets
  `TestAppGraph` exclude *only* the engine binding without touching the rest of
  `ApiModule`.

## Adding a fixture for a new endpoint

1. Find the Last.fm API method string the endpoint sends. e.g.
   `chart.getTopTracks` → `chart.gettoptracks` (lowercased URL parameter).
2. Drop a JSON file under one of these locations (precedence matches asset
   merge order in `app/build.gradle.kts`):
   - `app/src/test/assets/` — preferred when the same fixture is also useful
     for unit tests (this dir is shared into androidTest's classpath).
   - `app/src/androidTest/assets/` — e2e-only fixtures.
   For realistic data, prefer copying from an existing `*RepositorySpec.kt` or
   `data/api/src/test/assets/`. Real captured payloads catch parsing drift that
   a hand-written stub won't.
3. Map the method to the file in `FixtureDispatcher.fixtureName()`:
   ```kotlin
   "chart.gettoptracks" -> "chart_top_tracks.json"
   ```
4. Comparisons use `lowercase()`, so the URL casing (`getTopTracks` vs
   `gettoptracks`) doesn't matter — match against the lowercased form.

If a request hits an unmapped method, the dispatcher returns `501 Not
Implemented` with the method name in the body. That surfaces in the test as a
parse failure / empty UI, which is usually loud enough to find.

## Adding a new flow to `AppSmokeTest`

```kotlin
@Test
fun some_flow() {
  composeRule.waitUntilExactlyOneExists(hasText("anchor"), TIMEOUT_MS)
  // interactions
}
```

Conventions used by the existing flow:

- `@Before resetState()` calls `resetDataStores()` so previous runs don't leak
  a logged-in session into the next test.
- Anchor on **hardcoded English** strings (`"Let me in!"`, `"Home"`) rather
  than on translated `stringResource`s. The CI emulator is en-US by default,
  but local devices may not be — anchoring on translated text wedges the test
  on a non-English locale. If a flow has no hardcoded anchor, look up the
  string via `composeRule.activity.getString(R.string.foo)`.
- Use `hasSetTextAction()` matchers to drive `SunsetTextField` rather than
  searching by label — labels are part of a different semantic node from the
  input field.
- `TIMEOUT_MS = 5_000L` covers a CI cold emulator's first render + initial
  mocked API roundtrip. Tighten only if you have a specific reason.

## Running on CI

`.github/workflows/e2e_test.yml`:
- Schedule: `0 6 * * 5` (Friday 06:00 UTC = Friday 15:00 JST). Also
  `workflow_dispatch` — pick the target branch via "Use workflow from"
  in the GitHub Actions UI.
- Runs the Gradle Managed Device task `:app:pixel6Api35DebugAndroidTest`
  directly. Gradle/UTP owns the emulator lifecycle (boot, install, run, tear
  down) instead of `reactivecircus/android-emulator-runner`. The previous
  runner was prone to "device offline" failures mid-test under combined
  `adb screenrecord` + UTP load.
- AVDs (`~/.android/avd`) and the system image
  (`/usr/local/lib/android/sdk/system-images`) are cached via `actions/cache`
  with key `gmd-pixel6-api35-google-x86_64-v1`. Bump the key when the device
  spec or system image changes.
- Failures upload `**/build/reports/androidTests/**`,
  `**/build/outputs/androidTest-results/**`, and
  `app/build/outputs/e2e-recordings/**` as artifacts (7-day retention).
- A screen recording is captured via `adb shell screenrecord` streamed
  directly to a host file (`--output-format=h264 ... -` redirected to
  `app/build/outputs/e2e-recordings/e2e.h264`). Streaming to the host —
  rather than to `/sdcard` followed by `adb pull` — is what lets the
  recording survive GMD tearing down the emulator at task end.

## Gotchas worth remembering

- **Don't override `android:name` via `tools:replace` in the androidTest
  manifest.** It looks like the right knob, but it makes the test APK try to
  instantiate `TestApp` from its *own* package's classloader — which can't see
  `App` from the main APK. Symptom: `ClassNotFoundException: ...App` (the
  parent class, not TestApp itself). Use `MetroTestRunner.newApplication`
  instead — that path goes through `MetroAppComponentFactory` in the target
  process and resolves both APKs correctly.
- **Don't share `HttpClientEngine` provider with `ApiModule`'s other
  bindings.** If `provideHttpClientEngine` lived inside `ApiModule`, excluding
  it would also kill `provideOkhttpClient` / `provideLastFmService` /
  `provideImageLoader`. The split into `HttpEngineModule` is what keeps the
  exclusion surgical.
- **Compose's `createAndroidComposeRule` (v1)** is deprecated in favour of v2
  but still works. The v2 API uses `StandardTestDispatcher` which changes
  coroutine ordering — migrate intentionally, not casually.

## What's intentionally not covered

- Production API drift. The mock returns whatever JSON we last captured; if
  Last.fm changes a schema, this suite happily passes. Catching that needs a
  separate (non-weekly, opt-in) job that hits the real API.
- Login error paths, edge cases, deep-linked navigation. Those live in unit /
  screenshot tests where they're cheaper.
- `feature:account` Play Store / file-system dependencies. They aren't mocked
  — the screen happens to render fine without `appUpdateInfo`, but if a future
  account flow blocks on Play Core, plan to provide a fake `AppUpdateManager`
  in `MockApiModule`.
