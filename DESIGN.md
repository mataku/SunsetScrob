# DESIGN.md

Design system reference for SunsetScrob. Companion to [`CLAUDE.md`](CLAUDE.md)
(architecture) and [`docs/coding-conventions.md`](docs/coding-conventions.md)
(naming / Compose style). This file documents the **visual identity, theme
intent, and UI component conventions** so that contributors (human or AI) can
add screens that look consistent with the rest of the app.

The runtime source of truth for tokens lives in code, not here:

- Color palette: [
  `ui_common/.../style/Colors.kt`](ui_common/src/main/java/com/mataku/scrobscrob/ui_common/style/Colors.kt)
- Per-theme color schemes & accents: [
  `ui_common/.../style/SunsetTheme.kt`](ui_common/src/main/java/com/mataku/scrobscrob/ui_common/style/SunsetTheme.kt)
- Typography: [
  `ui_common/.../style/SunsetTextStyle.kt`](ui_common/src/main/java/com/mataku/scrobscrob/ui_common/style/SunsetTextStyle.kt)
- Theme enum: [
  `core/.../entity/AppTheme.kt`](core/src/main/java/com/mataku/scrobscrob/core/entity/AppTheme.kt)

Where this doc and code disagree, **code wins** — update this doc.

---

## Overview

SunsetScrob is a Last.fm client centered on listening history (scrobbles),
charts, and album/artist exploration. The visual mood is **dark-first,
artwork-forward, low-chrome**: album art is the hero on most surfaces, and the
UI chrome (bars, headers, dividers) recedes so artwork and text content carry
the page.

Core principles:

1. **Dark by default.** `AppTheme.DARK` is the default; four of five themes
   are dark. Light theme is supported but secondary.
2. **Material 3 is wrapped, not imported.** Outside `:ui_common`, code never
   imports `androidx.compose.material3.*` directly — every Material 3
   component is fronted by a `SunsetX` wrapper that lives in `:ui_common`.
   `:lint-checks` enforces this with one `PreferSunsetX` import-based detector
   per wrapper, and CI fails on violations. Theming still goes through
   `MaterialTheme` with our `ColorScheme`s; don't introduce a parallel
   theming layer.
3. **One theme provider.** Outside `:ui_common`, themable composables read
   colors via `LocalAppTheme.current.<role>Color()` (`backgroundColor()`,
   `primaryColor()`, `surfaceColor()`, `accentColor()`, …). The
   `PreferLocalAppThemeColor` lint detector blocks direct
   `MaterialTheme.colorScheme.*` access from feature / `:app` code; only
   `:ui_common` bridges between Material 3 and the Sunset color API.
4. **Artwork is content, not decoration.** Image loading is centralized in
   `SunsetImage` so placeholder, crossfade, and error behavior stay uniform.
5. **AI-assistability matters.** Conventions here exist so that adding a new
   screen by analogy to an existing one produces a result that fits the rest
   of the app.

---

## Themes

There are five themes, declared in
[`AppTheme.kt`](core/src/main/java/com/mataku/scrobscrob/core/entity/AppTheme.kt)
and applied in
[`SunsetTheme.kt`](ui_common/src/main/java/com/mataku/scrobscrob/ui_common/style/SunsetTheme.kt).
The user picks one in account settings; `ThemeRepository` persists it; the
root `SunsetTheme(theme = ...)` propagates the chosen `ColorScheme` and a
matching `LocalAppTheme`.

| Theme            | `isLight` | Intent                                                         | Surface / Background                                         | Accent (`accentColor()`)         |
|------------------|-----------|----------------------------------------------------------------|--------------------------------------------------------------|----------------------------------|
| `DARK` (default) | false     | Neutral dark — the canonical look the app is designed against. | `Colors.ContentBackground` (`#263238`)                       | `Colors.LightLime`               |
| `LIGHT`          | true      | Bright variant for users who don't want a dark UI.             | `#F5F5F5`                                                    | `Colors.LightLime`               |
| `MIDNIGHT`       | false     | Pure-black OLED-friendly variant.                              | `Color.Black`                                                | `Colors.LightLime`               |
| `OCEAN`          | false     | Deep-blue accent variant for users who want a colored dark.    | `Colors.DeepOceanBackground` (`#191c38`) / surface `#37474F` | `Colors.DeepOcean`               |
| `LASTFM_DARK`    | false     | Last.fm-flavored dark, accented in Last.fm red.                | `#37474F` / background `Colors.ContentBackground`            | `Colors.LastFmColor` (`#bb0414`) |

Notes:

- `priority` on `AppTheme` controls picker ordering: `DARK (1) → MIDNIGHT (2)
  → OCEAN (3) → LASTFM_DARK (4) → LIGHT (5)`.
- `DARK` and `LASTFM_DARK` use named token objects (`DarkColor`,
  `LastFmDarkColor`); `MIDNIGHT` and `OCEAN` are inlined inside
  `SunsetTheme.kt` instead of having their own `*Color` object. If you add a
  sixth theme, prefer the named-object pattern.
- `accentColor()` is **not** part of `ColorScheme`. Use it for theme-specific
  highlight colors (e.g. selected indicators) where Material 3's
  `primary`/`tertiary` mapping doesn't fit.
- Ripple is overridden per `isLight` via `LocalRippleConfiguration`. Don't
  override ripple again locally — call sites get it from `SunsetTheme`.

### Color usage rules

- In feature / `:app` composables, read colors via
  `LocalAppTheme.current.<role>Color()`. The available extensions live in
  `ui_common/.../style/SunsetTheme.kt`:

  | Extension              | Source                                     |
  |------------------------|--------------------------------------------|
  | `backgroundColor()`    | `colorScheme().background`                 |
  | `primaryColor()`       | `colorScheme().primary`                    |
  | `onPrimaryColor()`     | `colorScheme().onPrimary`                  |
  | `onSecondaryColor()`   | `colorScheme().onSecondary`                |
  | `surfaceColor()`       | `colorScheme().surface`                    |
  | `onSurfaceColor()`     | `colorScheme().onSurface`                  |
  | `accentColor()`        | per-theme accent (`LightLime` / `DeepOcean` / `LastFmColor`) |

  Direct `MaterialTheme.colorScheme.*` access is banned outside
  `:ui_common` by the `PreferLocalAppThemeColor` lint detector. If a role
  you need isn't exposed yet, add a new `<role>Color()` extension in
  `SunsetTheme.kt` rather than reaching into `MaterialTheme.colorScheme`.
- Reach into `Colors.X` (the raw palette) **only** inside
  `style/` definitions or for theme-agnostic constants like the heart
  color. New screens should not import `Colors` directly.
- Status-bar tints use `Colors.StatusBarDark` / `Colors.StatusBarLight`
  (semi-transparent overlays, not solid colors).

---

## Typography

All text styles live in
[`SunsetTextStyle`](ui_common/src/main/java/com/mataku/scrobscrob/ui_common/style/SunsetTextStyle.kt)
and use the bundled **Noto Sans JP** family (Regular / Medium / Bold). All
styles set `includeFontPadding = false` to keep vertical rhythm tight.

| Style      | Size / Weight                | Use                                                              |
|------------|------------------------------|------------------------------------------------------------------|
| `title`    | 20sp Medium                  | Top-level screen titles in `NavigationHeader` / `ContentHeader`. |
| `headline` | 18sp Medium                  | Section headers within a screen.                                 |
| `subtitle` | 16sp Medium                  | Sub-headers, list-section titles.                                |
| `body`     | 16sp Regular                 | Default body copy.                                               |
| `label`    | 14sp Regular                 | Compact labels, secondary list lines.                            |
| `button`   | 14sp Medium, 1.25sp tracking | Action labels (uses `onSecondary`).                              |
| `caption`  | 13sp Regular                 | Tertiary metadata, timestamps (uses `onSecondary`).              |

Composable-getter styles (`caption`, `button`) read
`LocalAppTheme.current.onSecondaryColor()` at composition time, so they
re-skin per theme automatically. The plain `val`-style entries (`body`,
`label`, `title`, etc.) inherit color from the surrounding
`LocalContentColor` — set color explicitly on the parent or on the
`SunsetText` if you need a non-default.

Don't introduce ad-hoc `TextStyle(...)` literals at call sites — extend
`SunsetTextStyle` instead.

---

## Components

UI primitives under `:ui_common` are organized by responsibility, not by
atomic-design layer:

```
ui_common/.../
  component/   — Material 3 wrappers (SunsetText, SunsetButton, SunsetSurface,
                 SunsetTopAppBar, SunsetTextField, …) and app-specific
                 composables (NavigationHeader, ContentHeader, SunsetImage,
                 FilteringBottomSheet, LoadingIndicator, CircleBackButton, …).
                 These are the only files allowed to import
                 androidx.compose.material3.*.
  screen/      — full-screen reusable composables (e.g. WebViewScreen).
  style/       — Colors, SunsetTheme, SunsetTextStyle, color extensions.
  extension/   — reusable Modifier / Spanned / Duration helpers.
```

When a feature module needs a UI piece that's also useful elsewhere, **lift
it into `:ui_common`** rather than copy-pasting. Material 3 wrappers and
shared widgets both go under `component/`; full-screen composables go under
`screen/`.

### When to use what

| Need                                                     | Use                                                                                     | File                                                          |
|----------------------------------------------------------|-----------------------------------------------------------------------------------------|---------------------------------------------------------------|
| Remote image (album art, avatar, etc.)                   | `SunsetImage` (Coil 3, with placeholder + crossfade). Don't call `AsyncImage` directly. | `component/SunsetImage.kt`                                    |
| Top app bar / screen title                               | `NavigationHeader` or `ContentHeader`                                                   | `component/NavigationHeader.kt`, `component/ContentHeader.kt` |
| Bottom navigation                                        | `SunsetNavigationBar`                                                                   | `component/SunsetNavigationBar.kt`                            |
| Pageable filter UI                                       | `FilteringBottomSheet` + `FilteringFloatingButton`                                      | `component/`                                                  |
| Loading state inside a list/page                         | `LoadingIndicator` (small) / `InfiniteLoadingIndicator` (paging tail)                   | `component/`                                                  |
| Back button on artwork hero                              | `CircleBackButton` (translucent over imagery)                                           | `component/CircleBackButton.kt`                               |
| Wiki / bio block                                         | `SimpleWiki` or `WikiCell`                                                              | `component/`                                                  |
| Tab labels in `TabRow`                                   | `TabRowText`                                                                            | `component/TabRowText.kt`                                     |
| Embedded web content (release notes, OSS licenses, etc.) | `WebViewScreen`                                                                         | `screen/WebViewScreen.kt`                                     |

### Composition locals

Three composition locals are provided by `SunsetTheme` and can be read
without prop-drilling:

- `LocalAppTheme` — current `AppTheme`. Primary color-access entry point
  outside `:ui_common`: call `LocalAppTheme.current.<role>Color()` for
  background / primary / surface / accent / etc., and use it for `isLight`
  branching when needed.
- `LocalSnackbarHostState` — single shared snackbar host. Use this instead of
  creating a new `SnackbarHostState` per screen.
- `LocalTopAppBarState` — `TopAppBarScrollBehavior` for the current top app
  bar. Throws if read outside a screen that provides one (by design).

### Animation / sizing constants

Defined in `SunsetTheme.kt`; reuse rather than redefine:

- `ANIMATION_DURATION_MILLIS = 700`
- `TRANSITION_ANIMATION_DURATION_MILLIS = 600`
- `BOTTOM_APP_BAR_HEIGHT = 80.dp`

---

## Custom wrappers (SunsetX)

Every Material 3 component used by the app is fronted by a `SunsetX` wrapper
in `:ui_common`. Outside `:ui_common`, `androidx.compose.material3.*` imports
are banned — call `SunsetText`, `SunsetButton`, `SunsetSurface`, etc. instead.
This keeps Material upgrades, theme tweaks, and design-system-wide changes in
one place, and lets feature modules depend on a stable surface that doesn't
shift with each Material release.

### Allowed imports outside `:ui_common`

`:feature/*` and `:app` modules should only import from:

```
com.mataku.scrobscrob.ui_common.*       # SunsetText, SunsetButton, ... wrappers
androidx.compose.foundation.*           # Layout, Modifier, foundation primitives
androidx.compose.runtime.*              # @Composable, remember, State
androidx.compose.ui.*                   # Modifier, Color, etc. (Material-free)
```

`androidx.compose.material3.*` is reserved for `:ui_common` internals. The
build files reflect this: feature modules declare `libs.compose.foundation`
explicitly and do **not** depend on `libs.compose.material3`. Only `:ui_common`
pulls in `compose-material3`.

### Wrapper API patterns

Two shapes are used, picked by what call sites actually look like:

**1. Single function** — when there's one canonical usage, no shared variants.
Example: `SunsetButton`, `SunsetSurface`.

```kotlin
@Composable
fun SunsetSurface(
  modifier: Modifier = Modifier,
  shadowElevation: Dp = 0.dp,
  content: @Composable () -> Unit,
) { Surface(modifier, shadowElevation = shadowElevation, content = content) }
```

**2. `object` + factory methods** — when several short, repeated variants exist
across call sites. Expose a base `operator fun invoke(...)` plus named factory
composables for each variant. Example: `SunsetText` (`Body`/`Label`/`Title`/
`Headline`/`Subtitle`/`Caption`/`ButtonLabel`), `SunsetTextButton` (`Label`).

```kotlin
object SunsetText {
  @SuppressLint("ComposeNamingUppercase")
  @Composable
  operator fun invoke(text: String, style: TextStyle, ...) { /* base */ }

  @Composable
  fun Body(text: String, color: Color = LocalContentColor.current, ...) { ... }

  @Composable
  fun Title(text: String, ...) { ... }
}
```

`@SuppressLint("ComposeNamingUppercase")` is required on `operator fun invoke`
because Slack `compose-lint` rejects lowercase composables and `invoke` can't
be renamed with a leading uppercase letter.

Keep the public API minimal. Add parameters only when multiple call sites need
them; if `SunsetTextStyle.X.copy(...)` already absorbs the difference, don't
expose it. Add later when a real second call site appears.

### Adding a new wrapper

Follow this sequence when a new `androidx.compose.material3.*` component
slips into a feature or `:app`:

1. **Wrapper**: add `ui_common/src/main/java/com/mataku/scrobscrob/ui_common/SunsetX.kt`.
   Choose single-function vs `object` + factory based on call-site shape.
2. **Detector**: add `lint-checks/.../PreferSunsetXDetector.kt`. Use any
   existing `PreferSunsetX*Detector.kt` as a template — they're all
   import-based scanners with the same shape: ban
   `androidx.compose.material3.X` outside `com.mataku.scrobscrob.ui_common`
   and its sub-packages, severity `ERROR`, suppression via
   `@Suppress("PreferSunsetX")` / `@file:Suppress(...)`.
3. **Registry**: add `PreferSunsetXDetector.ISSUE` to
   `lint-checks/.../SunsetIssueRegistry.kt` (alphabetical).
4. **Spec**: add `lint-checks/src/test/.../PreferSunsetXDetectorSpec.kt`. Use
   any existing spec as a template; the 6-case shape is fixed (ui_common
   allowed, ui_common sub-package allowed, feature reported, app reported,
   `@file:Suppress` opts out, unrelated material3 import is clean). All
   cases must call `.skipTestModes(TestMode.IMPORT_ALIAS)` — `IMPORT_ALIAS`
   rewrites imports as `import ... as IMPORT_ALIAS_1_X`, which double-counts
   for import-based detectors.
5. **Stub**: add `material3XStub` to `lint-checks/src/test/.../Stubs.kt`.
6. **Migrate** existing call sites: grep `androidx.compose.material3.X`,
   replace imports with the `SunsetX` wrapper, replace `style = SunsetTextStyle.body.copy(color = Y)`-style call sites with the matching preset
   (`SunsetText.Body(color = Y)`) where one exists, leave slot/content forms
   alone. Run `CI=true ./gradlew lintDebug` and confirm 0 errors.

VRT goldens stay unchanged as long as the wrapper's defaults match the bare
material3 default. Default-shifting wrappers (e.g. `SunsetText` defaulting to
`SunsetTextStyle.body` instead of `LocalTextStyle.current`) will rebase
goldens — verify with `fastlane screenshot_test` and update goldens as
needed.

---

## Do's and Don'ts

**Do**

- Read colors via `LocalAppTheme.current.<role>Color()`
  (`backgroundColor`, `primaryColor`, `onSurfaceColor`, `accentColor`, …).
  Add a new extension in `SunsetTheme.kt` if a role is missing.
- Use `SunsetTextStyle` entries for text; extend it when a new role is needed.
- Use `SunsetImage` for any remote image.
- Use `SunsetX` wrappers from `:ui_common` for any Material 3 component. If
  `:ui_common` doesn't expose what you need, add a wrapper there + a
  `PreferSunsetX` lint detector pair (see "Custom wrappers" above) — don't
  introduce a one-off material3 import.
- Reuse `:ui_common` organisms/molecules; if the same widget appears in a
  second feature module, promote it to `:ui_common` instead of duplicating.
- Use `SunsetThemePreview` (not `SunsetTheme`) inside `@Preview` composables —
  it skips ripple wiring that previews don't need and provides the
  `SunsetSurface` background so previews don't need their own `Surface { }`
  wrapper.
- Showkase annotations (`@ShowkaseColor`, `@ShowkaseTypography`) on new tokens
  so they appear in the design catalog.

**Don't**

- Don't import `androidx.compose.material3.*` from a feature or `:app`
  module. The matching `PreferSunsetX` lint detector will fail CI. If you
  truly need to (rare — usually means a wrapper is missing), suppress
  with `@Suppress("PreferSunsetX")` and document why in the same commit.
- Don't add `libs.compose.material3` to a feature module's `build.gradle.kts`.
  Foundation primitives come from `libs.compose.foundation`, animation from
  `libs.compose.animation`. Only `:ui_common` depends on `compose-material3`.
- Don't hard-code `Color(0xFF…)` at a call site. Add it to `Colors` (or a
  per-theme `*Color` object) and wire it through `ColorScheme`.
- Don't construct one-off `TextStyle(...)` literals; extend `SunsetTextStyle`.
- Don't read raw palette objects (`Colors`, `DarkColor`, …) from feature
  modules. Go through `LocalAppTheme.current.<role>Color()`.
- Don't use `AsyncImage` / `Image(painter = rememberAsyncImagePainter(...))`
  directly — go through `SunsetImage` so placeholder, crossfade, error, and
  shape conventions stay consistent.
- Don't introduce a new bottom bar, top bar, or back button. Use the existing
  `SunsetNavigationBar` / `NavigationHeader` / `ContentHeader` /
  `CircleBackButton`.
- Don't override `LocalRippleConfiguration` locally; `SunsetTheme` already
  picks the right alpha per `isLight`.
- Don't add a sixth theme without also adding a dedicated `*Color` object
  alongside `DarkColor` / `LightColor` / `LastFmDarkColor` and updating
  `accentColor()` + `colorScheme()` in `SunsetTheme.kt`.
