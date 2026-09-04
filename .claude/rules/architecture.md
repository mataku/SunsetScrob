# Architecture

This file is loaded on every session. Path-scoped detail rules:

- `viewmodel.md` — auto-loads when reading any `*ViewModel.kt` or files under `**/viewmodel/`
- `repository.md` — auto-loads when reading any `*Repository*.kt`, files under `**/repository/`, or files under `**/data/`

Read those files explicitly when planning ViewModel or Repository work,
since path-scoped rules don't fire until matching files are opened.

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
feature/
  album/                - Album details
  artist/               - Artist details
  auth/                 - Login authentication
  scrobble/             - Scrobble history
  account/              - Account settings
  discover/             - Charts and discovery
  home/                 - Home tab (integrates other features)
```

```
feature/* ──→ data/repository ──→ data/api ──→ core
    │                │
    │                └──→ data/db ──→ core
    │
    └──→ ui_common ──→ core
```

## Module Dependency Rules

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

## Package Structure

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

## Source Sets (KMP modules)

- `src/commonMain/kotlin` — everything that has no Android dependency. Screens, ViewModels, repositories, entities.
- `src/androidMain/kotlin` — Android-only code and every Metro binding container (`di/*Module.kt`), because `:app` is Android and provides `Context`. Also `AndroidManifest.xml` and `res/` for resources that unmigrated Android modules still read through `R`.
- `src/jvmMain/kotlin` — JVM-only actuals (fonts loaded from the classpath, text style shims).
- `src/commonMain/composeResources` — drawables and strings read through the generated `Res` class.
- `src/jvmTest/kotlin` — Kotest specs and JVM screenshot tests. No `src/test`.
- `expect`/`actual` is used only where the platform API differs (`toReadableIntValue`, `notoSansJpFontFamily`, `noFontPaddingPlatformTextStyle`). Prefer injecting a platform object (`SqlDriver`, `DataStore<Preferences>`, `AppBuildInfo`) over `expect` declarations.

## Convention Plugins

If you create a new module, **apply an existing convention plugin** rather
than hand-rolling configuration:

- `FeatureConventionPlugin` — Android library feature modules (SDK, Kotlin,
  lint, packaging, test deps).
- `ApplicationConventionPlugin` — the `:app` module.
- `ComposeConventionPlugin` — modules that use Compose.
- `MetroConventionPlugin` — modules that participate in the Metro DI graph.
- `ScreenshotTestConventionPlugin` — modules that contribute Roborazzi screenshots.

Plugin IDs (used in `build.gradle.kts`):

| Plugin ID                             | Purpose                                                                          |
|---------------------------------------|-----------------------------------------------------------------------------------|
| `sunsetscrob.library`                 | KMP library (android + jvm targets, SDK levels, jvmTest dependencies, VRT tags)  |
| `sunsetscrob.compose`                 | Compose Multiplatform, compiler plugin, Compose Resources (`Res` is public)      |
| `sunsetscrob.metro`                   | Metro DI (`dev.zacsweers.metro`, adds `metrox-viewmodel-compose`)                |
| `sunsetscrob.test.screenshot`         | Roborazzi on the JVM (`recordRoborazziJvm` / `verifyRoborazziJvm`)               |
| `sunsetscrob.android.application`     | For app module                                                                   |
| `sunsetscrob.android.feature`         | Android-only feature modules that have not been migrated to KMP yet              |
| `sunsetscrob.android.compose`         | Compose for Android-only modules                                                 |
| `sunsetscrob.android.metro`           | Metro for Android-only modules                                                   |
| `sunsetscrob.android.test.screenshot` | Roborazzi via Robolectric for Android-only modules                               |

### New Feature Module Example

Reference: [ui_common/build.gradle.kts](../../ui_common/build.gradle.kts); feature modules still on sunsetscrob.android.* follow the older shape until they migrate

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
- Direct dependency on Navigation 3 (`androidx.navigation3.*`) is allowed only
  inside `:ui_common`. Feature modules consume the DSL exposed by
  `:ui_common/.../navigation/SunsetNavBuilder` and friends. Enforced by Konsist
  (`NavigationArchitectureSpec`).
- Direct dependency on `androidx.browser` (Auth Tab / Custom Tabs) is allowed
  only inside `:feature:auth`, behind `LastFmWebAuthLauncher`. Enforced by
  Konsist (`ModuleDependencyArchitectureSpec`).

## Error Handling

- `core/entity/presentation/SunsetResult.kt` exists as a result type but is
  **not** the default in UIs today. The repository returns raw `Flow<T>`, and
  the VM converts errors to `UiEvent.Error` via `.catch { e -> ... }`.
- Do not scatter `try/catch` blocks across VMs or composables. Centralize on
  the Flow boundary.
