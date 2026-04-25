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
  `ui_common/.../SunsetTextStyle.kt`](ui_common/src/main/java/com/mataku/scrobscrob/ui_common/SunsetTextStyle.kt)
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
2. **Material 3, lightly customized.** Theming goes through `MaterialTheme`
   with our `ColorScheme`s. Don't introduce a parallel theming layer.
3. **One theme provider.** All themable composables read from
   `MaterialTheme.colorScheme` (and `LocalAppTheme` when accent or
   theme-shape branching is genuinely required).
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

- Read all colors via `MaterialTheme.colorScheme.*` in composables.
- Reach into `Colors.X` (the raw palette) **only** inside
  `style/` definitions or for theme-agnostic constants like the heart
  color. New screens should not import `Colors` directly.
- Status-bar tints use `Colors.StatusBarDark` / `Colors.StatusBarLight`
  (semi-transparent overlays, not solid colors).

---

## Typography

All text styles live in
[`SunsetTextStyle`](ui_common/src/main/java/com/mataku/scrobscrob/ui_common/SunsetTextStyle.kt)
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

Composable-getter styles (`caption`, `button`) read `MaterialTheme.colorScheme`
at composition time, so they re-skin per theme automatically. The plain
`val`-style entries (`body`, `label`, `title`, etc.) inherit color from
the surrounding `LocalContentColor` — set color explicitly on the parent or on
the `Text` if you need a non-default.

Don't introduce ad-hoc `TextStyle(...)` literals at call sites — extend
`SunsetTextStyle` instead.

---

## Components

UI primitives are organized atomic-design-ish under `:ui_common`:

```
ui_common/.../
  component/   — small primitives (one widget, almost no logic)
  molecule/    — composed primitives (layout + 1–2 components)
  organism/    — bar/header/sheet-level units
  template/    — full-screen reusable scaffolds
  style/       — Colors, SunsetTheme
```

When a feature module needs a UI piece that's also useful elsewhere, **lift
it into `:ui_common`** at the matching layer rather than copy-pasting.

### When to use what

| Need                                                     | Use                                                                                     | File                                                        |
|----------------------------------------------------------|-----------------------------------------------------------------------------------------|-------------------------------------------------------------|
| Remote image (album art, avatar, etc.)                   | `SunsetImage` (Coil 3, with placeholder + crossfade). Don't call `AsyncImage` directly. | `molecule/SunsetImage.kt`                                   |
| Top app bar / screen title                               | `NavigationHeader` or `ContentHeader`                                                   | `organism/NavigationHeader.kt`, `organism/ContentHeader.kt` |
| Bottom navigation                                        | `SunsetNavigationBar`                                                                   | `organism/SunsetNavigationBar.kt`                           |
| Pageable filter UI                                       | `FilteringBottomSheet` + `FilteringFloatingButton`                                      | `organism/`, `molecule/`                                    |
| Loading state inside a list/page                         | `LoadingIndicator` (small) / `InfiniteLoadingIndicator` (paging tail)                   | `molecule/`, `organism/`                                    |
| Back button on artwork hero                              | `CircleBackButton` (translucent over imagery)                                           | `component/CircleBackButton.kt`                             |
| Wiki / bio block                                         | `SimpleWiki` or `WikiCell`                                                              | `molecule/`                                                 |
| Tab labels in `TabRow`                                   | `TabRowText`                                                                            | `molecule/TabRowText.kt`                                    |
| Embedded web content (release notes, OSS licenses, etc.) | `WebViewScreen`                                                                         | `template/WebViewScreen.kt`                                 |

### Composition locals

Three composition locals are provided by `SunsetTheme` and can be read
without prop-drilling:

- `LocalAppTheme` — current `AppTheme`. Read this when you need
  `accentColor()` or `isLight` branching that `MaterialTheme` doesn't expose.
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

## Do's and Don'ts

**Do**

- Read colors via `MaterialTheme.colorScheme.*`; read accents via
  `LocalAppTheme.current.accentColor()`.
- Use `SunsetTextStyle` entries for text; extend it when a new role is needed.
- Use `SunsetImage` for any remote image.
- Reuse `:ui_common` organisms/molecules; if the same widget appears in a
  second feature module, promote it to `:ui_common` instead of duplicating.
- Use `SunsetThemePreview` (not `SunsetTheme`) inside `@Preview` composables —
  it skips ripple wiring that previews don't need.
- Showkase annotations (`@ShowkaseColor`, `@ShowkaseTypography`) on new tokens
  so they appear in the design catalog.

**Don't**

- Don't hard-code `Color(0xFF…)` at a call site. Add it to `Colors` (or a
  per-theme `*Color` object) and wire it through `ColorScheme`.
- Don't construct one-off `TextStyle(...)` literals; extend `SunsetTextStyle`.
- Don't read raw palette objects (`Colors`, `DarkColor`, …) from feature
  modules. Go through `MaterialTheme.colorScheme` or `accentColor()`.
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
