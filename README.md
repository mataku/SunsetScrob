## SunsetScrob

Last.fm client on Android (mainly for my portfolio)

- - -


![](./screenshot/features.jpg)

See more: [screenshots](./screenshot/README.md)

## Features

- Show latest scrobbles
- Show top albums
- Show track content details
- Show album content details
- Filter top albums by time range
- Show top artists
- Filter top artists by time range
- Update NowPlaying and Scrobbling (for Apple Music, Spotify, and YouTube Music only)
- Switch themes
- Autofill
- Themed icon

## How to try

Prepare local.properties and run `./gradlew installDebug`

```
API_KEY=YOUR LAST.FM API KEY
SHARED_SECRET=YOUR LAST.FM SHARED SECRET
```

- - -

or try via Google Play

## Libraries and tools

- Jetpack Compose
- Kotlin Coroutines
- Ktor client
- Coil
- Gradle version catalog
- Gradle convention plugin
- fastlane
- Kotest

## Quality

Conventions are encoded as executable specs and enforced at the CI boundary,
so any change is verified the same way before it lands.

| Category     | Tool / Module                      | Trigger   |
|--------------|------------------------------------|-----------|
| Unit         | Kotest + MockK                     | push / PR |
| Architecture | Konsist (`:architecture-spec`)     | push / PR |
| Lint         | Android Lint                       | push / PR |
| Custom Lint  | `:lint-checks` (project detectors) | push / PR |
| VRT          | Roborazzi screenshot tests         | push / PR |
| E2E          | Compose integration tests          | weekly    |
| Compose perf | Compose compiler metrics           | weekly    |
| Startup perf | Macrobenchmark                     | weekly    |

### Architecture spec

`:architecture-spec` is a Konsist sensor module. Each rule under
`.claude/rules/` is paired with a `*ArchitectureSpec.kt` test, so violations
of module-graph and layering rules fail the build. Examples:

- `:feature:*` must not depend on `:data:api` / `:data:db` directly
- Feature-to-feature dependencies are forbidden, except `:feature:home`
  which acts as the navigation hub
- Repositories must return `Flow`
- Every rule doc under `.claude/rules/` is paired with a Spec — guide and
  sensor stay in sync by construction

### Android Lint

Lint runs with AGP defaults plus targeted disables in
`AndroidLintConfiguration` (`build-logic/convention/.../ext/`). It fails the
build only when `CI=true` — locally, issues surface via reports and IDE
highlights. Project-specific rules are implemented as custom detectors in
`:lint-checks`, registered through the Compose convention plugin so every
Compose module picks them up:

- `@Preview` composables must be `private`
- `UiState` must be `@Immutable` and exposed as `StateFlow`
- Use the project's `Sunset*` wrappers instead of Material3 primitives directly
  (`SunsetText`, `SunsetButton`, `SunsetTopAppBar`, …)

Lint failures break CI on PR, and a summary is auto-posted as a PR comment.

### UI quality

UI changes are guarded at three layers — pixel-level regression on every
render, mock-API end-to-end smoke on the real APK, and a single source of
truth for components / navigation in `:ui_common` so call sites don't drift.

#### Visual regression (Roborazzi)

Roborazzi renders every screen to PNG and diffs against goldens checked
into each module's `screenshot/`. The verify task runs on push / PR, so
unintended layout shifts fail before review. Phone and tablet variants
both have goldens. Regenerate intentional changes with the `record` task.

#### End-to-end against a mocked API

Smoke instrumentation tests in `app/src/androidTest/` drive the real APK
against a Ktor `MockEngine` that serves canned JSON fixtures. A weekly CI
job runs them on Gradle Managed Devices for both phone and tablet form
factors, so adaptive layout breakage surfaces outside Roborazzi's static
renders. Schema drift against the real Last.fm API is intentionally out
of scope. Setup and fixture conventions live in
[`.claude/rules/e2e-testing.md`](.claude/rules/e2e-testing.md).

#### Shared UI primitives in `:ui_common`

Material 3 components and Navigation 3 APIs are fronted by `Sunset*`
wrappers in `:ui_common`. Direct `androidx.compose.material3.*` and
`androidx.navigation3.*` imports outside `:ui_common` are blocked by
`:lint-checks` detectors and Konsist specs, so theme tweaks, Material /
Nav upgrades, and transition tuning land in one place. The full
component, navigation, and theming spec is in [`DESIGN.md`](DESIGN.md).

<details>
<summary> module graph </summary>

![](./screenshot/module_graph.png)

</details>
