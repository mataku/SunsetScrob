---
paths:
  - "build-logic/**"
  - "config/detekt/**"
  - "**/build.gradle.kts"
  - "gradle.properties"
---

# Build Logic and Code Quality Tooling

The core rule (configuration lives in convention plugins under `build-logic/`, the root `build.gradle.kts` is declaration-only) is in `architecture.md` under "Convention Plugins". This file holds the detail needed only when editing build scripts, convention plugins or the detekt config.

## Root build script sensor

Only the `subprojects { }` and `allprojects { }` forms are enforced, and only by a plain-text check: `BuildScriptArchitectureSpec` is a Kotest `DescribeSpec` that reads the root `build.gradle.kts` with `File.readText()` and matches two regexes against it, with no Konsist or PSI parsing involved. Any other configuration in the root script — a bare `dependencies { }`, `tasks.withType { }`, `extensions.configure { }` — is caught by review only, so treat this rule as broader than its sensor.

## detekt

`DetektConfiguration.kt` sets `buildUponDefaultConfig = true` and leaves `disableDefaultRuleSets = false`, so detekt's bundled rule sets run alongside the `Compose` and `ktlint` plugin rule sets, and `config/detekt/detekt.yml` is where each one is switched on or off. `buildUponDefaultConfig` means a rule the YAML omits runs with detekt's shipped default instead of silently going inactive, which is why every bundled rule set is listed with an explicit `active` flag and, inside the two that are on (`coroutines`, `exceptions`), every rule is listed too: only `GlobalCoroutineUsage`, `SwallowedException` and `TooGenericExceptionCaught` are active, each paired with the CLAUDE.md DON'T it enforces. detekt runs without type resolution (`detekt.android.disabled` / `detekt.multiplatform.disabled` in `gradle.properties`), so a bundled rule that needs it cannot be turned on. Config validation (`config > validation`) is on by default, so removing a plugin from `detektPlugins` fails loudly, e.g. dropping `io.nlopez.compose.rules` fails every module's detekt task with `Property 'Compose' is misspelled or does not exist ... may also indicate a detekt plugin is necessary`, and a bogus key under a bundled rule set or a bogus top-level rule set name fails the same way. Validation does not check rule keys under a rule set that a `detektPlugins` entry contributes — a typo'd or renamed key under `Compose:` or `ktlint:` is accepted silently with no build failure, so a rule renamed or removed by an upgrade of either plugin will pass unnoticed and must be caught by reading that plugin's release notes; the `active: false` keys pinned nearby only make a future default flip visible in a diff, they do not catch a rename.

## Android Lint and CI

Android Lint runs on `:app` only with built-in checks and no custom detectors. It fails the build only when `CI=true` (see `build-logic/convention/.../AndroidLintConfiguration.kt`) and runs in `.github/workflows/lint.yml` as a non-blocking step after detekt in the same job (skipped when detekt fails); detekt fails on any finding. KMP modules have no lint task, so Android Lint covers `:app` alone; detekt and Konsist are what cover every module. Kotlin compiler warnings are errors in every module (`KotlinWarningsConfiguration.kt` via `sunsetscrob.lint`), so a deprecation surfaced by a dependency bump fails the build instead of accumulating.
