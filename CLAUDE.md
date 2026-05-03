# CLAUDE.md

Guidance for AI coding agents (Claude Code and similar) working in this repository.
This document is the **inferential feedforward** part of the project's harness; most
rules below have a matching Konsist sensor under `:architecture-spec` that will fail
CI if violated. When in doubt, follow the existing code in the module you are editing.

---

## Documentation

Detailed conventions are split into `.claude/rules/`. Some are loaded every
session (no `paths:` frontmatter); others load only when Claude reads matching
files (`paths:`-scoped).

| File                                  | Scope                                                              | Loaded                                                           |
|---------------------------------------|--------------------------------------------------------------------|------------------------------------------------------------------|
| `.claude/rules/architecture.md`       | Module deps, package structure, convention plugins, error handling | every session                                                    |
| `.claude/rules/coding-conventions.md` | Naming, Compose conventions, navigation, security                  | every session                                                    |
| `.claude/rules/viewmodel.md`          | ViewModel conventions                                              | when reading `*ViewModel.kt` or `**/viewmodel/**`                |
| `.claude/rules/repository.md`         | Repository conventions, Metro DI                                   | when reading `*Repository*.kt`, `**/repository/**`, `**/data/**` |
| `.claude/rules/testing.md`            | Unit + Screenshot test conventions                                 | when reading `*Spec.kt` / `*Test.kt`                             |
| `.claude/rules/e2e-testing.md`        | E2E (instrumentation) test architecture                            | when reading `app/src/androidTest/**`                            |
| `DESIGN.md`                           | UI/Theme intent + `:ui_common` wrapping (Material 3, Navigation 3, adaptive) | explicit Read only                                     |

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
- `./gradlew testDebugUnitTest -PexcludeScreenshotTest=true` — run unit tests
- `./gradlew verifyRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true` — run
  Roborazzi screenshot tests
- `./gradlew recordRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true` generate
  golden images
- `make generate_compose_reports` — generate Compose compiler reports

---

## DO / DON'T Summary

These reminders are kept here because their detail rule is path-scoped
(`viewmodel.md`, `repository.md`, `testing.md`) and won't load until Claude
reads a matching file. Layer / package / Compose / navigation rules already
live in always-loaded `architecture.md` and `coding-conventions.md`.

**DO**

- Wire new dependencies through Metro with `@Binds` / `@Provides` on a
  `@ContributesTo(AppScope::class)` interface in `di/FooModule.kt`.
- Write a `*Spec.kt` Kotest test next to the class under test.

**DON'T**

- Use `GlobalScope` or a custom `CoroutineScope` inside a ViewModel.
- Emit one-shot events via a separate `SharedFlow`. Use the in-state
  `events: List<UiEvent>` + `popEvent` pattern the rest of the codebase uses.
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
- Code quality: Android Lint (project-specific custom detectors in
  `:lint-checks`), Compose lint (Slack `compose-lint-checks`), Konsist
  architecture tests, Licensee. Lint fails the build only when `CI=true`
  (see `build-logic/convention/.../AndroidLintConfiguration.kt`); CI runs
  it as a standalone job (`.github/workflows/lint.yml`).

---

## Architecture Tests (`:architecture-spec`)

Architecture rules are mechanically enforced by [Konsist](https://docs.konsist.lemonappdev.com/)
in the `:architecture-spec` module. Each rule under `.claude/rules/` maps to a
`*ArchitectureSpec.kt` under
`architecture-spec/src/test/kotlin/com/mataku/scrobscrob/architecture/`.

- Run locally: `./gradlew :architecture-spec:test`
- Run in CI: `./gradlew :architecture-spec:test` via the GitHub Actions
  workflow `arch_test.yml`, independent of the main test job.

When you add a new convention to a `.claude/rules/` file, add a matching Spec.
When you change a Spec, update the corresponding rule. Guide and sensor must
stay paired.
