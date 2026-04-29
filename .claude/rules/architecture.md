# Architecture

This file is loaded on every session. Path-scoped detail rules:

- `viewmodel.md` — auto-loads when reading any `*ViewModel.kt` or files under `**/viewmodel/`
- `repository.md` — auto-loads when reading any `*Repository*.kt`, files under `**/repository/`, or files under `**/data/`

Read those files explicitly when planning ViewModel or Repository work,
since path-scoped rules don't fire until matching files are opened.

## Module Overview

```
app/                    - Application entry point, Metro AppGraph, Navigation
core/                   - Entity definitions (bottom layer, referenced by all modules)
ui_common/              - Shared Composables, theme, colors
data/
  api/                  - Last.fm API client, Endpoint definitions
  db/                   - DataStore (local persistence)
  repository/           - Repository interface + Impl
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

| Plugin ID                             | Purpose                                                                   |
|---------------------------------------|---------------------------------------------------------------------------|
| `sunsetscrob.android.application`     | For app module                                                            |
| `sunsetscrob.android.feature`         | For feature / library modules                                             |
| `sunsetscrob.android.compose`         | Compose configuration                                                     |
| `sunsetscrob.android.metro`           | Metro DI (applies `dev.zacsweers.metro`, adds `metrox-viewmodel-compose`) |
| `sunsetscrob.android.test.screenshot` | Roborazzi tests                                                           |

### New Feature Module Example

Reference: [feature/album/build.gradle.kts](../../feature/album/build.gradle.kts)

```kotlin
plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.metro")
  id("sunsetscrob.android.test.screenshot")
}

android {
  namespace = "com.mataku.scrobscrob.newfeature"
}

dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))

  implementation(libs.compose.navigation)
  implementation(libs.coroutines)
  implementation(libs.kotlinx.collection)
}
```

Rules:

- Use convention plugins for common configuration.
- Write only module-specific configuration in `build.gradle.kts`.

## Error Handling

- `core/entity/presentation/SunsetResult.kt` exists as a result type but is
  **not** the default in UIs today. The repository returns raw `Flow<T>`, and
  the VM converts errors to `UiEvent.Error` via `.catch { e -> ... }`.
- Do not scatter `try/catch` blocks across VMs or composables. Centralize on
  the Flow boundary.
