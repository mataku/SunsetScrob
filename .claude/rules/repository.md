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
- Use `@Provides` on a `companion object` function when construction needs
  logic or third-party types. Scope with `@SingleIn(AppScope::class)` as needed.
- The root graph is `app/.../di/AppGraph.kt`:
  `@DependencyGraph(AppScope::class) interface AppGraph : MetroAppComponentProviders, ViewModelGraph, ScrobbleServiceDependencies`.
  `App` creates it via `createGraphFactory<AppGraph.Factory>().create(this)`.
