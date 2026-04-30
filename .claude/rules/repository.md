---
paths:
  - "**/*Repository*.kt"
  - "**/repository/**/*.kt"
  - "**/data/**/*.kt"
---

# Repository & Metro DI

## Repository Conventions

Reference: `data/repository/.../ScrobbleRepository.kt`, `data/repository/di/RepositoryModule.kt`.

- Interface and its `Impl` class live in the **same file**, same package.
- Methods return `Flow<T>`; wrap async work with `flow { ... }.flowOn(Dispatchers.IO)`.
  Enforced by the `RepositoryReturnsFlow` Lint detector in `:lint-checks`.
  Suppress with `@Suppress("RepositoryReturnsFlow")` only for genuine
  synchronous accessors that cannot be Flow-shaped (currently
  `UsernameRepository.username()`, used as a field initializer).
- Bind all repositories in `data/repository/di/RepositoryModule.kt` with
  `@Binds` and `@SingleIn(AppScope::class)`. The interface is annotated
  `@ContributesTo(AppScope::class)` so Metro auto-aggregates it into the
  app graph — no explicit `includes` wiring is needed; `:data:api`'s
  `ApiModule` and `:data:db`'s `DatabaseModule` join automatically because
  they are also `@ContributesTo(AppScope::class)`.
- Do not catch errors inside the repository. Let them propagate — the
  ViewModel's `.catch { ... }` maps them to a `UiEvent.Error`.

## Metro Binding Container Conventions

- Binding containers are Kotlin `interface`s, live in a `di` subpackage, and
  are named `FooModule.kt` / `FooModule` (the `*Module` suffix remains to
  match existing structure).
- Annotate every binding container with `@ContributesTo(AppScope::class)`
  (from `dev.zacsweers.metro.*`). Metro discovers and merges all
  contributions automatically — there is no equivalent of Hilt's
  `@InstallIn(...)` or `@Module(includes = [...])`.
- Use `@Binds` on interface methods for interface-to-impl bindings. Pair
  with `@SingleIn(AppScope::class)` for app-scoped singletons.
- **Exception for `internal` impls.** A public `@ContributesTo(AppScope::class)`
  interface cannot expose an `internal` type via a `@Binds` method (Kotlin
  visibility). When the `Impl` class needs to stay `internal` (to keep it
  unreachable from sibling modules), write the binding as an `internal`
  `@Provides` function on the `companion object` instead. Reference:
  `:data:api/.../LastFmService.kt` (`internal class LastFmServiceImpl`) and
  the binding in `:data:api/.../di/ApiModule.kt`.
- Use `@Provides` on a `companion object` function when construction needs
  logic or third-party types. Scope with `@SingleIn(AppScope::class)` as needed.
- The root graph is `app/.../di/AppGraph.kt`:
  `@DependencyGraph(AppScope::class) interface AppGraph : MetroAppComponentProviders, ViewModelGraph, ScrobbleServiceDependencies`.
  `App` creates it via `createGraphFactory<AppGraph.Factory>().create(this)`.

## Repository Spec Conventions

Reference: `data/repository/src/test/.../AlbumRepositorySpec.kt`,
`data/repository/src/test/.../ScrobbleRepositorySpec.kt`.

- Repository unit tests **mock the `LastFmService` interface** with mockk; do
  not construct `LastFmServiceImpl` or wire up Ktor's `MockEngine`. Capture
  the `Endpoint` passed to `rawRequest` with `slot<Endpoint<*>>()`.
- Stub the service via `coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse`,
  where `fakeResponse` is a hand-built API response model using **named
  arguments** for every field. Mappers are exercised transitively through the
  emitted `Flow` value.
- Assert both:
  - the captured endpoint's concrete subclass via `slot.captured.shouldBeInstanceOf<FooEndpoint>()`, and
  - the captured `params` via `slot.captured.params shouldBe mapOf(...)`. The
    expected map's value types must match the production code's types
    exactly (e.g. `Int` vs `String` for `page` / `limit`).
- For branch logic that should NOT call the API (e.g. early-return on
  null/empty session key, on a not-yet-scrobbled track), assert with
  `coVerify(exactly = 0) { service.rawRequest(any(), any()) }`.
- Assert at least one **field** on the emitted entity (not just
  `isNotEmpty()`), so the mapper is genuinely exercised.
- Construct ancillary mocks (e.g. `ArtworkDataStore`) with explicit `coEvery`
  stubs rather than `mockk(relaxed = true)`, unless the call genuinely is
  unused by the test path.
- **Never construct `LastFmServiceImpl`, `MockEngine`, or any other Ktor
  type inside a repository spec.** That responsibility belongs to the
  corresponding `:data:api` Endpoint spec — see the `*EndpointSpec.kt`
  files under `data/api/src/test/.../endpoint/`. Enforced by
  `RepositoryTestArchitectureSpec` in `:architecture-spec`.
