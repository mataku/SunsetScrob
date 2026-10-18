---
paths:
  - "app/src/androidTest/**"
  - "app/src/smoke/**"
---

# Smoke Testing

Smoke-level instrumentation tests that drive the R8-minified `smoke` build of the app against a mocked Last.fm API.
The goal is "does the app still launch, log in, and render core screens" — anything finer-grained belongs in unit tests or Roborazzi screenshot tests (see [`testing.md`](testing.md)).

The tests live in `app/src/androidTest/`; the mocked graph and fixtures they run against live in the `smoke` build type's source set, `app/src/smoke/`.
Runs on CI via `.github/workflows/smoke_test.yml`: on pull requests that touch build configuration, dependencies, `:app`, any `androidMain` source set or the smoke harness itself (see the `paths` filter in the workflow), on a daily schedule against `develop`, and on demand via `workflow_dispatch`.
There is no run on push to `develop`: the PR run already covers each change, and breakage that only appears once several branches are merged is left to the daily run.
The daily run executes even when `develop` has not moved, on purpose: the runner image, emulator and other parts of the environment change independently of the repository.

## Commands

| Action | Command |
|--------|---------|
| Run locally on a connected emulator | `./gradlew :app:connectedSmokeAndroidTest` |
| Run locally on the phone GMD used by CI | `./gradlew :app:pixel6Api35SmokeAndroidTest` |
| Run only the tablet (`@LargeScreenSmoke`) tests on the tablet GMD | `./gradlew :app:pixelTabletApi35SmokeAndroidTest -PincludeLargeScreenSmoke=true` |
| Build the smoke APK and the test APK | `./gradlew :app:assembleSmoke :app:assembleSmokeAndroidTest` |

`pixel6Api35` and `pixelTabletApi35` are declared in `ApplicationConventionPlugin.kt` via `testOptions.managedDevices.allDevices` (Pixel 6 / Pixel Tablet, API 35, AOSP-ATD x86_64).
First local run downloads the system image and creates the AVD; subsequent runs reuse it.

GMD stores its AVDs under `~/.android/avd/gradle-managed/`, and the setup phase boots each one once to write a `default_boot` snapshot so later runs resume instead of cold booting.
CI restores that directory per device via `.github/actions/restore-avd-cache` and refreshes it via `.github/actions/save-avd-cache`.
The AVD cache and the smoke Gradle build cache are written only by the daily scheduled run on `develop` and are read-only everywhere else, so the repository stays inside the 10 GB Actions cache budget.
The cache key is fixed (`gmd-avd-<os>-<device>-develop`); when a device definition in `ApplicationConventionPlugin.kt` changes, the stale AVD is restored but `cleanManagedDevices --unused-only` (run at the top of each `scripts/run_smoke_*.sh`) deletes it and GMD recreates it, so the cache is self-healing.

## `@LargeScreenSmoke` — tablet-only tests

Tests annotated `@com.mataku.scrobscrob.app.testing.LargeScreenSmoke` require a tablet-sized device and would otherwise fail / no-op on the phone GMD.
The annotation lives in the app's androidTest source set (`app/src/androidTest/.../LargeScreenSmoke.kt`) — NOT in `:test_helper:integration`.
Pulling that module onto the androidTest classpath would drag Robolectric / Roborazzi onto the real-emulator classpath and break Espresso's idle checks.
The `defaultConfig` in `ApplicationConventionPlugin.kt` toggles `testInstrumentationRunnerArguments` based on the Gradle property `includeLargeScreenSmoke`:

- **default** (no property): `notAnnotation = ...LargeScreenSmoke` — excludes large-screen tests, so the phone CI run keeps working.
- **`-PincludeLargeScreenSmoke=true`**: `annotation = ...LargeScreenSmoke` — includes only large-screen tests. Pair with the tablet GMD task.

Inside the test class, request landscape via UiAutomator before composition reads window dimensions:

```kotlin
@Before
fun setUp() {
  val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
  device.setOrientationLandscape()
}
```

Reference: `app/src/androidTest/java/.../LargeScreenSmokeTest.kt`.

### The `smoke` build type

`ApplicationConventionPlugin.kt` declares `smoke` with `initWith(release)` and sets `testBuildType = "smoke"`, so every instrumentation task (`*SmokeAndroidTest`) runs against a build that R8 shrinks and optimizes exactly as it does release.
A keep rule missing from `proguard-rules.pro` therefore fails the smoke suite instead of the shipped app.

- Signed with the debug keystore and suffixed `.dev`, so it matches the dev entry in `google-services.json`. It installs over a hand-installed debug build of the same id.
- Crashlytics mapping upload is disabled for it in `app/build.gradle.kts`.
- AGP leaves every library the app already ships out of the instrumentation APK and shrinks the instrumentation APK against the smoke build's mapping (`minifySmokeAndroidTestWithR8`, `-applymapping`). Whatever the tests call that the app itself never calls would be removed or merged away by R8, so `app/smoke-rules.pro`, applied only to the `smoke` build type, keeps it:
  - the `App.appGraph` → `SessionRepository.logout()` path used by `resetDataStores()`;
  - the libraries that compose-ui-test, androidx.test and the tests link against (`androidx.compose.runtime` / `ui`, `androidx.activity` / `collection` / `concurrent.futures` / `core` / `lifecycle` / `tracing`, Guava's `util.concurrent`, `kotlin`, `kotlinx.coroutines`), with `allowobfuscation` so they are still renamed. Without them R8 full mode removes or merges more than a hundred classes the tests call, such as `kotlinx.coroutines.BuildersKt` and `androidx.compose.runtime.Composer`.
  - Everything else, including our code, Material 3, foundation, Ktor, kotlinx.serialization, Metro, Navigation 3 and Coil, is shrunk and optimized as in release, which is where missing keep rules actually bite.
- `src/smoke/` also carries an `AppInfoProviderImpl`, because that class is provided per build type (`debug`, `release`, `benchmark`, `smoke`).

## Architecture

The smoke build replaces the production `App` with `SmokeApp` through `app/src/smoke/AndroidManifest.xml` (`tools:replace="android:name"`).
`SmokeApp` builds a `SmokeAppGraph` that excludes the production `HttpClientEngine`, auth launcher and image loader bindings and aggregates the fakes contributed to `SmokeScope`.

```
app/src/smoke/AndroidManifest.xml (android:name = SmokeApp)
  └─ SmokeApp.newAppGraph() ─► SmokeAppGraph (scope AppScope, additionalScopes SmokeScope)
                                 ├─ excludes HttpEngineModule, AuthModule, ImageLoaderModule
                                 ├─ MockApiModule (MockEngine)
                                 │     └─ FixtureDispatcher
                                 │           └─ the app's own assets/*.json
                                 ├─ FakeWebAuthModule (LastFmWebAuthLauncher → fixed token)
                                 └─ FakeImageLoaderModule (Coil FakeImageLoaderEngine, solid colour)
```

The fakes contribute to `SmokeScope` rather than `AppScope` because the production `AppGraph` is compiled into the smoke variant as well; contributing to `AppScope` would give it a duplicate `HttpClientEngine` binding.
`SmokeScope` is the one exception to the `@ContributesTo(AppScope::class)` rule in `repository.md`.

Production-side hooks that make this possible:

- **`AppGraphContract`** (in `app/.../di/AppGraph.kt`) — neutral supertype shared by `AppGraph` and `SmokeAppGraph`. `App.appGraph` is typed against this contract so the two graphs are interchangeable.
- **`App.newAppGraph()`** is `internal open` — `SmokeApp` overrides it.
- **`HttpEngineModule`** (in `data/api/.../di/`) is a separate `@ContributesTo(AppScope::class)` interface whose only job is to provide `HttpClientEngine`. Keeping it in its own binding container is what lets `SmokeAppGraph` exclude *only* the engine binding without touching the rest of `ApiModule`.
- **`AuthModule`** (in `feature/auth/.../di/`) — binds `LastFmWebAuthLauncher`. Excluding it lets `FakeWebAuthModule` return `smoke_token` synchronously instead of opening a Custom Tab, so no test touches Chrome, last.fm, or `sunsetscrob.mataku.com`.
- **`ImageLoaderModule`** (in `data/api/.../di/`) — a separate `@ContributesTo(AppScope::class)` interface whose only job is to provide the Coil `ImageLoader`, split out of `ApiModule` for the same surgical- exclusion reason as `HttpEngineModule`. Excluding it lets `FakeImageLoaderModule` bind Coil's `FakeImageLoaderEngine`, so artwork renders as a solid colour instead of being fetched from `lastfm.freetls.fastly.net`.

## Adding a fixture for a new endpoint

1. Find the Last.fm API method string the endpoint sends. e.g. `chart.getTopTracks` → `chart.gettoptracks` (lowercased URL parameter).
2. Drop a JSON file under one of these locations; both are asset directories of the `smoke` source set (`ApplicationConventionPlugin.kt`), so the fixture ships inside the smoke APK:
   - `app/src/test/assets/` — preferred when the same fixture is also useful for unit tests.
   - `app/src/smoke/assets/` — smoke-only fixtures.

   For realistic data, prefer copying from an existing `*RepositorySpec.kt` or `data/api/src/jvmTest/resources/`.
   Real captured payloads catch parsing drift that a hand-written stub won't.

3. Map the method to the file in `FixtureDispatcher.fixtureName()`:
   ```kotlin
   "chart.gettoptracks" -> "chart_top_tracks.json"
   ```
4. Comparisons use `lowercase()`, so the URL casing (`getTopTracks` vs `gettoptracks`) doesn't matter — match against the lowercased form.

If a request hits an unmapped method, the dispatcher returns `501 Not Implemented` with the method name in the body.
That surfaces in the test as a parse failure / empty UI, which is usually loud enough to find.

## Adding a new flow to `AppSmokeTest`

```kotlin
@Test
fun some_flow() {
  composeRule.waitUntilExactlyOneExists(hasText("anchor"), TIMEOUT_MS)
  // interactions
}
```

Conventions used by the existing flow:

- `@Before resetState()` calls `resetDataStores()` so previous runs don't leak a logged-in session into the next test. It calls the running graph's `SessionRepository.logout()` rather than deleting files, because DataStore caches in-process across test classes.
- Anchor on **hardcoded English** strings (`"Sign in with Last.fm"`, `"Home"`) rather than on translated `stringResource`s. The CI emulator is en-US by default, but local devices may not be — anchoring on translated text wedges the test on a non-English locale. If a flow has no hardcoded anchor, look up the string via `composeRule.activity.getString(R.string.foo)`.
- Assert on text that comes from the fixture (`"PassCode"`, `"My Love Mine All Mine"`, `"smoke_user"`, `"About PassCode"`), not on a screen title such as `"Discover"`. A title renders even when every API call returned 501, so it proves nothing about parsing or the data flow; a fixture value only appears once the response was decoded and mapped.
- `AppSmokeTest` calls `composeRule.activityRule.scenario.recreate()` on a detail screen and re-asserts the same anchor. This covers Activity recreation (configuration change), where `SunsetNavBackStack` must restore the `@Serializable` key and the ViewModel survives via the retained store. It does not cover process death; that would need separate tooling and is out of scope.
- `TIMEOUT_MS` is `2_000L` in `AppSmokeTest` and `5_000L` in the tablet / web-auth tests. The phone value is deliberately tight (see PR #1112); raise it only with a measured reason.

## Running on CI

`.github/workflows/smoke_test.yml`:
- Triggers: `pull_request` filtered by `paths` (dependency / build-logic / `:app` / `androidMain` / harness changes), a daily schedule (06:00 UTC) on `develop`, and `workflow_dispatch` — for the manual run, pick the target branch via "Use workflow from" in the GitHub Actions UI. The `paths` filter means a PR outside those paths produces no smoke run at all, so the job must not be a required status check until the filter is replaced by an in-workflow change-detection step (a skipped job counts as success, a never-started one blocks the merge forever).
- Discord notification fires only for non-PR runs; a PR failure is already visible on the PR.
- Single `smoke` job fanned out via `strategy.matrix` into two parallel variants, whose check names are just `Phone` and `Tablet` because the workflow name already says `Smoke test` (`fail-fast: false`, so a tablet failure doesn't cancel the phone run and vice versa):
  - `pixel6Api35` (artifact-suffix `phone`) — runs the default `:app:pixel6Api35SmokeAndroidTest` task. The `notAnnotation` runner arg auto-skips `@LargeScreenSmoke` tests.
  - `pixelTabletApi35` (artifact-suffix `tablet`) — runs `:app:pixelTabletApi35SmokeAndroidTest -PincludeLargeScreenSmoke=true`, so only `@LargeScreenSmoke` tests run.
- Gradle/UTP owns the emulator lifecycle (boot, install, run, tear down) instead of `reactivecircus/android-emulator-runner`. The previous runner was prone to "device offline" failures mid-test under combined `adb screenrecord` + UTP load.
- The AVD cache is handled by the `restore-avd-cache` / `save-avd-cache` composite actions described above, keyed per matrix variant (`phone` / `tablet`). Gradle caches go through `restore-gradle-cache` / `save-gradle-cache` with `cache-key: smoke`.
- Failures upload `**/build/reports/androidTests/**`, `**/build/outputs/androidTest-results/**`, and `**/build/intermediates/managed_device_android_test_additional_output/**` as artifacts named `smoke-test-reports-{phone|tablet}` (7-day retention).

## Gotchas worth remembering

- **Swap the application class in `src/smoke/AndroidManifest.xml`, never in an androidTest manifest.** An androidTest `tools:replace` makes the test APK instantiate the application from its own classloader, which can't see `App` in the app APK (`ClassNotFoundException: ...App`). The smoke manifest is merged into the app APK itself, so `SmokeApp` loads from the same classloader as `App`.
- **A test that calls into the app or a new library may need a keep rule.** If an instrumentation run fails with `NoClassDefFoundError` / `NoSuchMethodError` / `NoSuchFieldError`, R8 removed something only the tests use; keep it in `app/smoke-rules.pro`, not in `proguard-rules.pro`, so release is unaffected.
- **Don't share `HttpClientEngine` provider with `ApiModule`'s other bindings.** If `provideHttpClientEngine` lived inside `ApiModule`, excluding it would also kill `provideOkhttpClient` / `provideLastFmService` / `provideImageLoader`. The split into `HttpEngineModule` is what keeps the exclusion surgical.
- **Compose's `createAndroidComposeRule` (v1)** is deprecated in favour of v2 but still works. The v2 API uses `StandardTestDispatcher` which changes coroutine ordering — migrate intentionally, not casually.

## What's intentionally not covered

- Production API drift. The mock returns whatever JSON we last captured; if Last.fm changes a schema, this suite happily passes. Catching that needs a separate, opt-in job that hits the real API.
- Login error paths, edge cases, deep-linked navigation. Those live in unit / screenshot tests where they're cheaper.
- The real browser round-trip (Partial Custom Tab, Digital Asset Links verification, Chrome fallback). `WebAuthCallbackTest` covers the App Link path by delivering the callback `Intent` directly via `Instrumentation.callActivityOnNewIntent` — not `startActivity`, due to an `ActivityScenario` teardown issue with `setIntent`; install-time `autoVerify` is OS behaviour and is not asserted.
- `feature:account` Play Store / file-system dependencies. They aren't mocked — the screen happens to render fine without `appUpdateInfo`, but if a future account flow blocks on Play Core, plan to provide a fake `AppUpdateManager` in `MockApiModule`.
