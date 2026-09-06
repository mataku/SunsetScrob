# Architecture

This file is loaded on every session.
Path-scoped detail rules:

- `viewmodel.md` — auto-loads when reading any `*ViewModel.kt` or files under `**/viewmodel/`
- `repository.md` — auto-loads when reading any `*Repository*.kt`, files under `**/repository/`, or files under `**/data/`

Read those files explicitly when planning ViewModel or Repository work, since path-scoped rules don't fire until matching files are opened.

## Module Overview

```
app/                    - Application entry point, Metro AppGraph, Navigation
core/                   - Entity definitions (bottom layer, referenced by all modules) (KMP: android + jvm)
ui_common/              - Shared Composables, theme, colors (KMP: android + jvm)
data/
  api/                  - Last.fm API client, Endpoint definitions (KMP: android + jvm)
  db/                   - DataStore (local persistence) (KMP: android + jvm)
  repository/           - Repository interface + Impl (KMP: android + jvm)
test_helper/
  unit/                 - Shared unit test fixtures (KMP: android + jvm)
  integration/          - Shared screenshot/integration test fixtures (KMP: android + jvm)
feature/                (all KMP: android + jvm)
  album/                - Album details
  artist/               - Artist details
  auth/                 - Login authentication
  scrobble/             - Scrobble history
  account/              - Account settings
  discover/             - Charts and discovery
  home/                 - Home tab (integrates other features)
```

Every library module is KMP; `:app` is the only Android-only module.

```
feature/* ──→ data/repository ──→ data/api ──→ core
    │                │
    │                └──→ data/db ──→ core
    │
    └──→ ui_common ──→ core
```

## Module Dependency Rules

The module graph is strictly directional.
Violations break `:architecture-spec:test`.

- `:core` — pure, depends on **nothing** in this project.
- `:ui_common` — may depend only on `:core`.
- `:data:api` and `:data:db` — must not depend on each other.
- `:data:repository` — depends on `:data:api` and `:data:db`.
- `:feature:*` — may depend on `:ui_common`, `:core`, `:data:repository` only. **Never depend on `:data:api` or `:data:db` directly from a feature module.**
- Feature-to-feature dependencies are forbidden with one exception: `:feature:home` is the navigation hub and may depend on other feature modules. No other `:feature:*` may depend on another `:feature:*`.
- `:app` — top of the graph; may depend on anything.
- `:architecture-spec` and `:test_helper:*` — orthogonal, not part of the production graph.

## Package Structure

- Root package is `com.mataku.scrobscrob.<subpackage>`. The subpackage does not always equal the module name — `:feature:discover` is mostly `com.mataku.scrobscrob.discover`, but `DiscoverKey` still sits in the older `com.mataku.scrobscrob.chart.ui.navigation`. Follow the existing package in the module.
- Within a feature module:
  - Screens: `...ui.screen`
  - ViewModels: `...ui.viewmodel`
  - Navigation: `...ui.navigation`
  - Small composables: `...ui.molecule`
  - Metro binding containers: `...di`
- In `:data:repository`:
  - Mappers: `...data.repository.mapper`
  - Metro binding containers: `...data.repository.di`

## Source Sets (KMP modules)

- `src/commonMain/kotlin` — everything that has no Android dependency. Screens, ViewModels, repositories, entities.
- `src/androidMain/kotlin` — Android-only code and every Metro binding container (`di/*Module.kt`), because `:app` is Android and provides `Context`. Also `AndroidManifest.xml`. There is no `res/`: drawables, strings and fonts live in `commonMain/composeResources`.
- `src/jvmMain/kotlin` — JVM-only actuals (fonts loaded from the classpath, text style shims).
- `src/commonMain/composeResources` — drawables and strings read through the generated `Res` class.
- `src/jvmTest/kotlin` — Kotest specs and JVM screenshot tests. No `src/test`.
- `expect`/`actual` is used only where the platform API differs (`toReadableIntValue`, `noFontPaddingPlatformTextStyle`, `SunsetWebView`, `EqualizerAnimation`). Prefer injecting a platform object (`SqlDriver`, `DataStore<Preferences>`, `AppBuildInfo`) or a small interface bound in `androidMain` / `:app` (`LastFmWebAuthLauncher`, `InAppUpdateManager`, `NotificationListenerPermission`) over `expect` declarations; an interface needs no JVM actual when only Android will ever implement it.

## Convention Plugins

Whenever you write Gradle configuration — creating a module, or adding a build-wide tool to existing ones — put it in a convention plugin under `build-logic/` rather than hand-rolling it in a build script. The root `build.gradle.kts` is declaration-only: it lists plugins with `apply false` and holds no configuration. Cross-cutting tools that apply regardless of module type go in `build-logic/convention/src/main/java/ext/<Tool>Configuration.kt` with a convention plugin that applies them — `AndroidLintConfiguration.kt` and `DetektConfiguration.kt` are the worked examples. Never reach for `subprojects { }` or `allprojects { }` in the root build script.

Only the `subprojects { }` and `allprojects { }` forms are enforced, and only by a plain-text check: `BuildScriptArchitectureSpec` is a Kotest `DescribeSpec` that reads the root `build.gradle.kts` with `File.readText()` and matches two regexes against it, with no Konsist or PSI parsing involved. Any other configuration in the root script — a bare `dependencies { }`, `tasks.withType { }`, `extensions.configure { }` — is caught by review only, so treat this rule as broader than its sensor.

`DetektConfiguration.kt` sets `buildUponDefaultConfig = true` alongside `disableDefaultRuleSets = true`, which is not the contradiction it looks like and must not be "cleaned up": `disableDefaultRuleSets` is what stops detekt's own rule sets from running, while `buildUponDefaultConfig` is what keeps a rule that `config/detekt/detekt.yml` omits running with the plugin's shipped default instead of silently going inactive — config validation itself (`config > validation`) is on by default regardless of either flag, so removing a plugin from `detektPlugins` fails loudly either way, e.g. dropping `io.nlopez.compose.rules` fails every module's detekt task with `Property 'Compose' is misspelled or does not exist ... may also indicate a detekt plugin is necessary`. Validation also fails loudly on a bogus key under a *bundled* rule set and on a bogus top-level rule set name, but it does not check rule keys under a rule set that a `detektPlugins` entry contributes — a typo'd or renamed key under `Compose:` or `ktlint:`, the only rule sets `detekt.yml` configures, is accepted silently with no build failure, so a rule renamed or removed by an upgrade of either plugin will pass unnoticed and must be caught by reading that plugin's release notes; the `active: false` keys pinned nearby only make a future default flip visible in a diff, they do not catch a rename.

The plugins below are the per-module-type conventions:

- `KmpLibraryConventionPlugin` — every library module (android + jvm targets, SDK levels, jvmTest deps).
- `KmpComposeConventionPlugin` — library modules that use Compose.
- `KmpMetroConventionPlugin` — library modules that participate in the Metro DI graph.
- `KmpScreenshotTestConventionPlugin` — library modules that contribute Roborazzi screenshots.
- `LintConventionPlugin` — detekt on every module.
- `ApplicationConventionPlugin`, `ComposeConventionPlugin`, `MetroConventionPlugin`, `AndroidLintConventionPlugin` — the `:app` module only.

Plugin IDs (used in `build.gradle.kts`):

| Plugin ID                             | Purpose                                                                          |
|---------------------------------------|-----------------------------------------------------------------------------------|
| `sunsetscrob.library`                 | KMP library (android + jvm targets, SDK levels, jvmTest dependencies, VRT tags)  |
| `sunsetscrob.compose`                 | Compose Multiplatform, compiler plugin, Compose Resources (`Res` is public); enables Android resources so `composeResources` are packaged into the APK assets |
| `sunsetscrob.metro`                   | Metro DI (`dev.zacsweers.metro`, adds `metrox-viewmodel-compose`)                |
| `sunsetscrob.test.screenshot`         | Roborazzi on the JVM (`recordRoborazziJvm` / `verifyRoborazziJvm`); pins the test JVM locale to `en_US` so Compose Resources translations render deterministically |
| `sunsetscrob.lint`                    | detekt (compose-rules + ktlint formatting) on every module                        |
| `sunsetscrob.android.application`     | `:app` only                                                                      |
| `sunsetscrob.android.compose`         | Compose for `:app` only                                                          |
| `sunsetscrob.android.metro`           | Metro for `:app` only                                                            |
| `sunsetscrob.android.lint`            | Android Lint, `:app` only                                                        |

### New Feature Module Example

Reference: [ui_common/build.gradle.kts](../../ui_common/build.gradle.kts) and [feature/home/build.gradle.kts](../../feature/home/build.gradle.kts)

```kotlin
plugins {
  id("sunsetscrob.library")
  id("sunsetscrob.compose")
  id("sunsetscrob.metro")
  id("sunsetscrob.test.screenshot")
}

kotlin {
  android {
    namespace = "com.mataku.scrobscrob.newfeature"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(project(":ui_common"))
      implementation(project(":core"))
      implementation(project(":data:repository"))
      implementation(libs.jetbrains.lifecycle.runtime.compose)
      implementation(libs.kotlinx.collections.immutable)
    }
  }
}
```

Rules:

- Use convention plugins for common configuration.
- Write only module-specific configuration in `build.gradle.kts`.
- Direct dependency on Navigation 3 (`androidx.navigation3.*`) is allowed only inside `:ui_common`. Feature modules consume the DSL exposed by `:ui_common/.../navigation/SunsetNavBuilder` and friends. Enforced by Konsist (`NavigationArchitectureSpec`).
- Direct dependency on `androidx.browser` (Auth Tab / Custom Tabs) is allowed only inside `:feature:auth`, behind `LastFmWebAuthLauncher`. Enforced by Konsist (`ModuleDependencyArchitectureSpec`).
- Material 3 is declared per module rather than by `sunsetscrob.compose`, and only `:ui_common` (plus `:test_helper:integration`, which needs `LocalRippleConfiguration` for deterministic screenshots) may depend on it. Feature modules consume the `SunsetX` wrappers from `:ui_common`. Enforced by Konsist (`ModuleDependencyArchitectureSpec`); see `DESIGN.md`.

## Error Handling

- `core/entity/presentation/SunsetResult.kt` exists as a result type but is **not** the default in UIs today. The repository returns raw `Flow<T>`, and the VM converts errors to `UiEvent.Error` via `.catch { e -> ... }`.
- Do not scatter `try/catch` blocks across VMs or composables. Centralize on the Flow boundary.
