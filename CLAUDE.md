# CLAUDE.md

Guidance for AI coding agents (Claude Code and similar) working in this repository.
This document is the **inferential feedforward** part of the project's harness; most
rules below have a matching Konsist sensor under `:architecture-spec` that will fail
CI if violated. When in doubt, follow the existing code in the module you are editing.

---

## Documentation

Refer to the following documents only when needed:

| File | Purpose | When to Reference |
|------|---------|-------------------|
| `@docs/architecture.md` | Module structure, dependencies, implementation patterns | When implementing new features |
| `@docs/coding-conventions.md` | Naming conventions, Compose guidelines | When writing code |
| `@docs/testing.md` | Unit Test, Screenshot Test guidelines | When writing tests |

## Project Overview

SunsetScrob is a Last.fm client Android application. Modular Kotlin app using
Jetpack Compose, MVVM + Repository pattern, Metro DI, and feature-based modules.

## Setup

Before building, create `local.properties` with Last.fm API credentials:

```
API_KEY=YOUR_LAST_FM_API_KEY
SHARED_SECRET=YOUR_LAST_FM_SHARED_SECRET
```

## Common Commands

- `./gradlew installDebug` — install debug build on a device
- `./gradlew assembleDebug` — build debug APK
- `./gradlew bundleRelease` — build release AAB
- `./gradlew :architecture-spec:test` — run architecture (Konsist) tests
- `fastlane test` — run unit tests
- `fastlane screenshot_test` — run Roborazzi screenshot tests
- `fastlane arch_test` — run architecture tests
- `make generate_compose_reports` — generate Compose compiler reports

---

## Module Dependency Rules (enforced by Konsist — Rule 1)

The module graph is strictly directional. Violations break `:architecture-spec:test`.

- `:core` — pure, depends on **nothing** in this project.
- `:ui_common` — may depend only on `:core`.
- `:data:api` and `:data:db` — must not depend on each other.
- `:data:repository` — depends on `:data:api` and `:data:db`.
- `:feature:*` — may depend on `:ui_common`, `:core`, `:data:repository` only.
  **Never depend on `:data:api` or `:data:db` directly from a feature module.**
- Feature-to-feature dependencies are forbidden with one exception:
  `:feature:home` is the navigation hub and may depend on other feature modules.
  No other `:feature:*` may depend on another `:feature:*`.
- `:app` — top of the graph; may depend on anything.
- `:architecture-spec` and `:test_helper:*` — orthogonal, not part of the production graph.

## Package Structure (enforced by Konsist — Rule 2)

- Root package is `com.mataku.scrobscrob.<subpackage>`. The subpackage does not
  always equal the module name — e.g. `:feature:discover` uses
  `com.mataku.scrobscrob.chart`. Follow the existing package in the module.
- Within a feature module:
  - Screens: `...ui.screen`
  - ViewModels: `...ui.viewmodel`
  - Navigation: `...ui.navigation`
  - Small composables: `...ui.molecule`
  - Metro binding containers: `...di`
- In `:data:repository`:
  - Mappers: `...data.repository.mapper`
  - Metro binding containers: `...data.repository.di`

---

## ViewModel Conventions (Rule 3)

Reference: `feature/home/.../ui/viewmodel/HomeViewModel.kt`, `feature/scrobble/.../ui/viewmodel/ScrobbleViewModel.kt`.

- Annotate with `@Inject`, `@ViewModelKey`, and `@ContributesIntoMap(AppScope::class)`
  (imports from `dev.zacsweers.metro.*` and `dev.zacsweers.metrox.viewmodel.ViewModelKey`).
  Constructor parameters are resolved by Metro; no `@Inject constructor` needed
  when `@Inject` is applied at the class level.
- Expose state as `MutableStateFlow<FooUiState>` with `private set` (not `.asStateFlow()` — matches existing style).
- `FooUiState` is a `data class`, annotated `@Immutable`, with `ImmutableList<T>`
  for list fields (`kotlinx.collections.immutable`).
- One-shot events are a `sealed class FooUiEvent` carried **inside the state**
  as `events: List<FooUiEvent>`. The UI pops them via a public function on the
  VM — use `popEvent(event)` (or `popEvent()` when the event type is trivial).
  Some older VMs use `consumeEvent(event)`; prefer `popEvent` for new code.
- Launch coroutines only via `viewModelScope.launch { }` or `.launchIn(viewModelScope)`.
  **Do not** create your own `CoroutineScope`, and do not use `GlobalScope`.
- Class name ends with `ViewModel` and extends `androidx.lifecycle.ViewModel`
  (or `AndroidViewModel` when an `Application` dependency is genuinely needed).
- Declare ViewModels as `internal`. ViewModels are never consumed across
  module boundaries — the Screen wires them via `metroViewModel()` inside the
  same module, and navigation crosses modules via public `fooGraph()`
  extensions, not via VM types.
- Exception: `TopAlbumsViewModel`, `TopArtistsViewModel`, `ScrobbleViewModel`
  are public because `:feature:home`'s `HomeScreen` instantiates them via
  `metroViewModel<T>()` to embed each as a tab (hub pattern).

## Screen / Content Separation (Rule 4)

Reference: `feature/scrobble/.../ui/screen/ScrobbleScreen.kt`, `feature/home/.../ui/screen/HomeScreen.kt`.

- Top-level composable is `fun FooScreen(viewModel: FooViewModel = metroViewModel(), navigateToX: (...) -> Unit, modifier: Modifier = Modifier)`.
  It is stateful: collects state with `.collectAsStateWithLifecycle()` and owns
  navigation callbacks.
- A `private fun FooContent(...)` takes **data only** (no ViewModel), is previewable, and contains the Compose layout.
- Prefer declaring the top-level Screen composable as `internal` when
  feasible (Screens aren't consumed across modules beyond the hub pattern).
  Not currently enforced by Konsist — the hub pattern in `:feature:home`
  forces `TopAlbumsScreen`, `TopArtistsScreen`, `ScrobbleScreen` to stay
  public, and templates under `:ui_common` (e.g. `WebViewScreen`) are
  deliberately public as they are reused across modules.
- Handle one-shot events in the Screen with
  `LaunchedEffect(uiState.events) { uiState.events.firstOrNull()?.let { event -> ...; viewModel.popEvent(event) } }`.

## Navigation Conventions (Rule 5)

Reference: `feature/home/.../HomeNavigation.kt`, `app/.../NavigationGraph.kt`.

- Routes are `const val FOO_DESTINATION = "foo"` strings. **Do not** introduce
  type-safe `Nav*` sealed classes unilaterally; follow the existing string
  route pattern.
- Each feature exposes `fun NavGraphBuilder.fooGraph(...)`; `:app` composes them
  in `NavigationGraph.kt`.
- Navigate helpers are extensions on `NavController`: `fun NavController.navigateToFoo(...)`.
- Deep link arguments are read in the VM via
  `savedStateHandle.get<String>("artistName")` etc.

## Repository Conventions (Rule 6)

Reference: `data/repository/.../ScrobbleRepository.kt`, `data/repository/di/RepositoryModule.kt`.

- Interface and its `Impl` class live in the **same file**, same package.
- Methods return `Flow<T>`; wrap async work with `flow { ... }.flowOn(Dispatchers.IO)`.
- Bind all repositories in `data/repository/di/RepositoryModule.kt` with
  `@Binds` and `@SingleIn(AppScope::class)`. The interface is annotated
  `@ContributesTo(AppScope::class)` so Metro auto-aggregates it into the
  app graph — no explicit `includes` wiring is needed; `:data:api`'s
  `ApiModule` and `:data:db`'s `DatabaseModule` join automatically because
  they are also `@ContributesTo(AppScope::class)`.
- Do not catch errors inside the repository. Let them propagate — the
  ViewModel's `.catch { ... }` maps them to a `UiEvent.Error`.

## Metro Binding Container Conventions (Rule 7)

- Binding containers are Kotlin `interface`s, live in a `di` subpackage, and
  are named `FooModule.kt` / `FooModule` (the `*Module` suffix remains to
  match existing structure).
- Annotate every binding container with `@ContributesTo(AppScope::class)`
  (from `dev.zacsweers.metro.*`). Metro discovers and merges all
  contributions automatically — there is no equivalent of Hilt's
  `@InstallIn(...)` or `@Module(includes = [...])`.
- Use `@Binds` on interface methods for interface-to-impl bindings. Pair
  with `@SingleIn(AppScope::class)` for app-scoped singletons.
- Use `@Provides` on a `companion object` function when construction needs
  logic or third-party types. Scope with `@SingleIn(AppScope::class)` as needed.
- The root graph is `app/.../di/AppGraph.kt`:
  `@DependencyGraph(AppScope::class) interface AppGraph : MetroAppComponentProviders, ViewModelGraph, ScrobbleServiceDependencies`.
  `App` creates it via `createGraphFactory<AppGraph.Factory>().create(this)`.

## Test Conventions (Rule 8)

- **Unit tests**: Kotest `DescribeSpec` (`describe` / `context` / `it`). Files
  end with `Spec.kt`. Register `extension(CoroutinesListener())` when testing
  suspend code. Mocks: MockK — prefer explicit `mockk<T>()` + `coEvery { } returns ...`;
  avoid `mockk(relaxed = true)` unless the intent is genuinely "ignore all
  unused members".
- **Flow tests**: Turbine — `flow.test { awaitItem() shouldBe ...; cancelAndConsumeRemainingEvents() }`.
- **Screenshot tests**: Roborazzi. Files end with `Test.kt` (typically
  `*ScreenTest.kt` for screens, `*Test.kt` for components). Annotate with
  `@RunWith(AndroidJUnit4::class)`, `@GraphicsMode(GraphicsMode.Mode.NATIVE)`,
  **and** `@Category(VRT::class)` (marker from `:test_helper:integration`).
  The `@Category` is what lets `fastlane test` / `fastlane screenshot_test`
  include or exclude screenshot tests via JUnit Platform tags (Vintage maps
  `@Category(VRT::class)` to the tag `com.mataku.scrobscrob.test_helper.integration.VRT`,
  which `TestConfiguration.kt` filters on). Without it the test silently runs
  in the wrong bucket. Use the
  `composeRule.captureScreenshot(appTheme, fileName) { ... }` helper from
  `:test_helper:integration`.
- One test file per class under test.

## Error Handling (Rule 9)

- `core/entity/presentation/SunsetResult.kt` exists as a result type but is
  **not** the default in UIs today. The repository returns raw `Flow<T>`, and
  the VM converts errors to `UiEvent.Error` via `.catch { e -> ... }`.
- Do not scatter `try/catch` blocks across VMs or composables. Centralize on
  the Flow boundary.

---

## DO / DON'T Summary

**DO**

- Put new code in the module that matches its layer (UI → `:feature:*`,
  data orchestration → `:data:repository`, HTTP → `:data:api`, DB → `:data:db`).
- Wire new dependencies through Metro with `@Binds` / `@Provides` on a
  `@ContributesTo(AppScope::class)` interface in `di/FooModule.kt`.
- Write a `*Spec.kt` Kotest test next to the class under test.
- When adding a Compose screen, split stateful `FooScreen` from stateless `FooContent`.

**DON'T**

- Depend on `:data:api` or `:data:db` from a `:feature:*` module. Go through `:data:repository`.
- Create feature-to-feature dependencies (only `:feature:home` may).
- Use `GlobalScope` or a custom `CoroutineScope` inside a ViewModel.
- Emit one-shot events via a separate `SharedFlow`. Use the in-state
  `events: List<UiEvent>` + `popEvent` pattern the rest of the codebase uses.
- Introduce a parallel navigation DSL (type-safe routes, sealed `Nav*`, etc.)
  without first migrating the existing string routes.
- Catch errors inside a repository to return a fallback value. Let the Flow
  fail; the VM handles it.

---

## Tech Stack (for reference)

- UI: Jetpack Compose + Material 3
- Networking: Ktor client
- DB: SQLDelight + DataStore Preferences
- Async: Kotlin Coroutines + Flow
- DI: Metro (Kotlin compiler plugin; Dagger annotation interop enabled)
- Image loading: Coil 3
- Build: Gradle Kotlin DSL, version catalog (`gradle/libs.versions.toml`),
  custom convention plugins in `build-logic/convention/`.
- Tests: Kotest (JUnit5 platform), MockK, Turbine, Roborazzi.
- Code quality: Android Lint (29 custom checks in `app/lint-checks.gradle`),
  Compose lint, Konsist architecture tests, Licensee.

## Convention Plugins (build-logic/convention)

If you create a new module, **apply an existing convention plugin** rather
than hand-rolling configuration:

- `FeatureConventionPlugin` — Android library feature modules (SDK, Kotlin,
  lint, packaging, test deps).
- `ApplicationConventionPlugin` — the `:app` module.
- `ComposeConventionPlugin` — modules that use Compose.
- `MetroConventionPlugin` — modules that participate in the Metro DI graph.
- `ScreenshotTestConventionPlugin` — modules that contribute Roborazzi screenshots.

---

## Architecture Tests (`:architecture-spec`)

Architecture rules are mechanically enforced by [Konsist](https://docs.konsist.lemonappdev.com/)
in the `:architecture-spec` module. Each rule above maps to a `*ArchitectureSpec.kt`
under `architecture-spec/src/test/kotlin/com/mataku/scrobscrob/architecture/`.

- Run locally: `./gradlew :architecture-spec:test`
- Run in CI: `fastlane arch_test` (separate GitHub Actions workflow
  `arch_test.yml`, independent of the main test job).

When you add a new convention to this file, add a matching Spec. When you
change a Spec, update the corresponding rule here. Guide and sensor must
stay paired.
