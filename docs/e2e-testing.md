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
| Build the test APK only | `./gradlew :app:assembleDebugAndroidTest` |

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

## Adding a new flow to `SmokeFlowTest`

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
- `TIMEOUT_MS = 10_000L` is generous for emulator boot + first network call.
  Don't tighten without reason.

## Running on CI

`.github/workflows/e2e_test.yml`:
- Schedule: `0 18 * * 0` (Sunday 18:00 UTC = Monday 03:00 JST). Also
  `workflow_dispatch` with a `branch` input.
- Uses `reactivecircus/android-emulator-runner` on API 34 / x86_64 with an AVD
  snapshot cached via `actions/cache`. First run primes the snapshot; later
  runs reuse it.
- Failures upload `**/build/reports/androidTests/**`,
  `**/build/outputs/androidTest-results/**`, and
  `app/build/outputs/e2e-recordings/**` as artifacts (7-day retention).
- A screen recording is captured via `adb shell screenrecord` while the
  test runs (`--bit-rate 800000 --size 540x960 --time-limit 180`, ~2–3 MB
  for a 30s flow). The recording is pulled regardless of test result so
  the script doesn't have to know success/failure, but it's only uploaded
  on failure via the `if: failure()` artifact step.

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
