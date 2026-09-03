# DESIGN.md

Design system reference for SunsetScrob. Companion to [`CLAUDE.md`](CLAUDE.md)
(architecture) and [`.claude/rules/coding-conventions.md`](.claude/rules/coding-conventions.md)
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

There are six themes, declared in
[`AppTheme.kt`](core/src/main/java/com/mataku/scrobscrob/core/entity/AppTheme.kt)
and applied in
[`SunsetTheme.kt`](ui_common/src/main/java/com/mataku/scrobscrob/ui_common/style/SunsetTheme.kt).
The user picks one in account settings; `ThemeRepository` persists it; the
root `SunsetTheme(theme = ...)` calls `theme.resolve(isSystemInDarkTheme())`
to collapse `FOLLOW_SYSTEM` into `DARK`/`LIGHT`, then propagates the chosen
`ColorScheme` and a matching `LocalAppTheme`.

| Theme            | `isLight`        | Intent                                                                          | Surface / Background                                         | Accent (`accentColor()`)         |
|------------------|------------------|---------------------------------------------------------------------------------|--------------------------------------------------------------|----------------------------------|
| `FOLLOW_SYSTEM`  | resolves at runtime | Resolves to `DARK` or `LIGHT` based on `isSystemInDarkTheme()`. No own scheme. | (resolved theme's)                                           | `Colors.LightLime`               |
| `DARK` (default) | false            | Neutral dark — the canonical look the app is designed against.                  | `Colors.ContentBackground` (`#263238`)                       | `Colors.LightLime`               |
| `LIGHT`          | true             | Bright variant for users who don't want a dark UI.                              | `#F5F5F5`                                                    | `Colors.LightLime`               |
| `MIDNIGHT`       | false            | Pure-black OLED-friendly variant.                                               | `Color.Black`                                                | `Colors.LightLime`               |
| `OCEAN`          | false            | Deep-blue accent variant for users who want a colored dark.                     | `Colors.DeepOceanBackground` (`#191c38`) / surface `#37474F` | `Colors.DeepOcean`               |
| `LASTFM_DARK`    | false            | Last.fm-flavored dark, accented in Last.fm red.                                 | `#37474F` / background `Colors.ContentBackground`            | `Colors.LastFmColor` (`#bb0414`) |

Notes:

- `priority` on `AppTheme` controls picker ordering, lowest first:
  `FOLLOW_SYSTEM (0) → DARK (1) → MIDNIGHT (2) → OCEAN (3) → LASTFM_DARK (4) → LIGHT (5)`.
  `displayName` is the user-facing label shown in the picker.
- `FOLLOW_SYSTEM` does not have its own `ColorScheme`; `AppTheme.resolve(...)`
  returns `DARK` or `LIGHT` and `SunsetTheme` keys off the resolved value.
  Always pass the resolved theme to `LocalAppTheme`.
- `DARK` and `LASTFM_DARK` use named token objects (`DarkColor`,
  `LastFmDarkColor`); `MIDNIGHT` and `OCEAN` are inlined as `private val`
  schemes inside `SunsetTheme.kt` instead of having their own `*Color`
  object. If you add a seventh theme that needs its own scheme, prefer the
  named-object pattern.
- `accentColor()` is **not** part of `ColorScheme`. Use it for theme-specific
  highlight colors (e.g. selected indicators) where Material 3's
  `primary`/`tertiary` mapping doesn't fit. `FOLLOW_SYSTEM` returns
  `LightLime` directly (it isn't routed through `resolve()` for accent).
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
  component/
    designsystem/ — Material 3 wrappers (SunsetText, SunsetButton,
                    SunsetTextButton, SunsetIcon, SunsetIconButton,
                    SunsetIconToggleButton, SunsetSurface, SunsetTopAppBar,
                    SunsetTextField, SunsetImage, SunsetNavigationBar,
                    SunsetScaffold, SunsetSnackbarHost, SunsetTabRow,
                    SunsetTab, SunsetSwitch, SunsetChip,
                    SunsetCircularProgressIndicator, SunsetHorizontalDivider,
                    SunsetAlertDialog, SunsetBottomSheet,
                    SunsetModalBottomSheet, SunsetFloatingActionButton,
                    SunsetPullToRefreshBox, SunsetListDetailScaffold).
    (root)        — app-specific shared composables (NavigationHeader,
                    ContentHeader, FilteringBottomSheet, LoadingIndicator,
                    CircleBackButton, …) that compose the `designsystem/`
                    wrappers.
  navigation/  — Nav 3 wrappers (SunsetNavKey, SunsetNavBuilder,
                    SunsetNavHost, SunsetTabHost, SunsetNavBackStack,
                    SunsetDestinationScope, SunsetTransitionSpec,
                    CommonKeys). See "Navigation wrappers (SunsetNav*)".
  screen/      — full-screen reusable composables (e.g. WebViewScreen).
  style/       — Colors, SunsetTheme, SunsetTextStyle, color extensions,
                    WindowAdaptive (`isCompactWidth()`).
  extension/   — reusable Modifier / Spanned / Duration helpers.
```

`androidx.compose.material3.*` (and `androidx.compose.material3.adaptive.*`)
imports are confined to `:ui_common`, not literally to `component/designsystem/`
— `style/SunsetTheme.kt` and `style/WindowAdaptive.kt` legitimately import
material3 too. The `PreferSunsetX` lint detectors enforce the module-level
boundary, not a directory-level one.

When a feature module needs a UI piece that's also useful elsewhere, **lift
it into `:ui_common`** rather than copy-pasting. Material 3 wrappers go under
`component/designsystem/`; app-specific shared widgets go under `component/`;
full-screen composables go under `screen/`.

### When to use what

| Need                                                     | Use                                                                                     | File                                                          |
|----------------------------------------------------------|-----------------------------------------------------------------------------------------|---------------------------------------------------------------|
| Remote image (album art, avatar, etc.)                   | `SunsetImage` (Coil 3, with placeholder + crossfade). Don't call `AsyncImage` directly. | `component/designsystem/SunsetImage.kt`                       |
| Top app bar / screen title                               | `NavigationHeader` or `ContentHeader`                                                   | `component/NavigationHeader.kt`, `component/ContentHeader.kt` |
| Bottom navigation                                        | `SunsetNavigationBar`                                                                   | `component/designsystem/SunsetNavigationBar.kt`               |
| Pageable filter UI                                       | `FilteringBottomSheet` + `FilteringFloatingButton`                                      | `component/`                                                  |
| Loading state inside a list/page                         | `LoadingIndicator` (small) / `InfiniteLoadingIndicator` (paging tail)                   | `component/`                                                  |
| Back button on artwork hero                              | `CircleBackButton` (translucent over imagery)                                           | `component/CircleBackButton.kt`                               |
| Wiki / bio block                                         | `SimpleWiki` or `WikiCell`                                                              | `component/`                                                  |
| Tab labels in `TabRow`                                   | `TabRowText`                                                                            | `component/TabRowText.kt`                                     |
| Embedded web content (release notes, OSS licenses, etc.) | `WebViewScreen`                                                                         | `screen/WebViewScreen.kt`                                     |

### Composition locals

Two composition locals live alongside `SunsetTheme` and can be read without
prop-drilling:

- `LocalAppTheme` — `staticCompositionLocalOf` set by `SunsetTheme` to the
  resolved `AppTheme`. Primary color-access entry point outside
  `:ui_common`: call `LocalAppTheme.current.<role>Color()` for background /
  primary / surface / accent / etc., and use it for `isLight` branching when
  needed.
- `LocalSnackbarHostState` — `staticCompositionLocalOf` defaulting to a fresh
  `SunsetSnackbarHostState` (the wrapper's own state class, not Material 3's
  `SnackbarHostState`). Use it instead of creating a state per screen so that
  any screen can post via `LocalSnackbarHostState.current` and the host is
  rendered once at the `:app` scaffold level via `SunsetSnackbarHost`.

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

1. **Wrapper**: add `ui_common/src/main/java/com/mataku/scrobscrob/ui_common/component/designsystem/SunsetX.kt`.
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
goldens — verify with `./gradlew verifyRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true`
and update goldens as needed.

---

## Navigation wrappers (SunsetNav*)

Navigation 3 (`androidx.navigation3.*`) is wrapped in `:ui_common` for the
same reason Material 3 is: feature and `:app` modules depend on a stable,
SunsetScrob-flavored navigation DSL, not on Nav 3 directly. This keeps Nav 3
upgrades, transition tuning, back-stack mechanics, and ViewModel scoping in
one place, and matches the "Material 3 is wrapped, not imported" principle
from the previous section.

Outside `:ui_common`, `androidx.navigation3.*` imports are banned, and Nav 2
(`androidx.navigation.*`, anywhere in the project) is fully removed. Both
constraints are enforced by `NavigationArchitectureSpec` in
`:architecture-spec`, not left to review.

### Wrapper inventory

All under `ui_common/.../navigation/`:

| API                                                       | Role                                                                                                       |
|-----------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| `SunsetNavKey`                                            | Marker interface for destinations. `Serializable`, `@Immutable`. Replaces Nav 3's `NavKey` at the API edge. |
| `SunsetNavBuilder`                                        | DSL receiver. Features call `destination<FooKey> { ... }` on it.                                           |
| `SunsetNavHost`                                           | Single-stack host. Wraps `NavDisplay` + `SharedTransitionLayout` + the standard slide / predictive-pop transitions. |
| `SunsetTabHost`                                           | Tabbed host: one `SunsetNavBackStack` per tab, saved state per tab. Used by `SunsetMainScreen`.            |
| `SunsetNavBackStack` / `rememberSunsetNavBackStack(...)`  | Saveable back stack created from an initial `SunsetNavKey`.                                                |
| `SunsetDestinationScope`                                  | Receiver inside a `destination { ... }` block. Exposes `navigate`, `popBackStack`, `animatedContentScope`, `viewModelFor<VM>(key)`, and `SharedTransitionScope` membership. |
| `SunsetTransitionSpec`                                    | `Slide` (default) or `SharedElement`. Per-destination transition selection on `destination<K>(transitionSpec = ...)`. |
| `CommonKeys.kt`                                           | Cross-feature keys living in `:ui_common` itself (`LoginKey`, `PrivacyPolicyKey`, `WebViewKey`).           |

`SunsetNavEntry` and `NavKeyExtras` are internal plumbing for back-stack
serialization and Metro VM extras — not part of the public surface, don't
reference them from feature code.

### Allowed imports outside `:ui_common`

`:feature/*` and `:app` modules consume the navigation DSL via:

```
com.mataku.scrobscrob.ui_common.navigation.*   # SunsetNavBuilder, SunsetNavKey, viewModelFor, ...
```

They do **not** import `androidx.navigation3.*` directly. Inside a
`*Navigation.kt` block, navigation actions are members of
`SunsetDestinationScope` — call `navigate(FooKey(...))` and `popBackStack()`
directly. Don't reach for a `NavController` or any other Nav 3 type.

ViewModel acquisition inside `*Navigation.kt` files goes through
`viewModelFor<FooViewModel>(key)` — direct `metroViewModel(...)` calls from
those files are blocked by `NavigationArchitectureSpec` so that the NavKey
flows into Metro's `CreationExtras`. See
`.claude/rules/viewmodel.md` for the assisted-injection pattern on the VM
side, and `.claude/rules/coding-conventions.md` "Navigation" for the full
call-site walkthrough.

### Adding a new wrapper

When SunsetScrob needs Nav 3 functionality that the current DSL doesn't
expose (a new entry decorator, a multi-back-stack shape, a different host,
etc.), mirror the SunsetX flow:

1. **Wrapper**: add the API under `ui_common/.../navigation/SunsetX.kt`.
   Keep the public surface minimal — accept only what real call sites need;
   add parameters later when a second call site appears.
2. **Spec**: extend `NavigationArchitectureSpec` if the new API introduces a
   constraint that should hold across the project (e.g. "only
   `*Navigation.kt` may call X", "all implementations of Y must be
   `@Immutable`"). Architecture rules are mechanical — encoded as Konsist,
   not left to review (see `feedback_mechanize_conventions`).
3. **Migrate**: grep `androidx.navigation3.X`, replace call sites with the
   wrapper, confirm `:architecture-spec:test` stays green.

Don't add `androidx.navigation3.*` imports to feature/`:app` modules even
temporarily — introduce the wrapper first.

---

## Tablet & adaptive layout

The app is designed phone-first but adapts to tablet (medium/expanded
width) by branching on width class and switching list-then-detail flows
into a two-pane scaffold. A single `:ui_common` wrapper —
`SunsetListDetailScaffold` — owns the adaptive logic so feature modules
never touch `androidx.compose.material3.adaptive.*` directly.

### Width branching

Screens that have a list-then-detail flow branch on
`com.mataku.scrobscrob.ui_common.style.isCompactWidth()`:

```kotlin
@Composable
fun TopAlbumsScreen(..., navigateToAlbumInfo, albumViewModelProvider, navigateToWebView, ...) {
  if (isCompactWidth()) {
    TopAlbumsCompact(... onAlbumTap = navigateToAlbumInfo, ...)
  } else {
    val scaffoldState = rememberSunsetListDetailScaffoldState<AlbumKey>()
    SunsetListDetailScaffold(
      state = scaffoldState,
      listPane = { TopAlbumsCompact(..., onAlbumTap = { album, id -> scaffoldState.selectDetail(AlbumKey(...)) }, useSharedElement = false) },
      detailPane = { selection -> if (selection != null) AlbumPaneScreen(id = "", viewModel = albumViewModelProvider(selection), ...) },
    )
  }
}
```

`isCompactWidth()` returns `true` when the window's `WindowWidthSizeClass`
is COMPACT (< 600dp) — a phone in any orientation, or a small foldable
inner. Anything wider goes through the scaffold path.

### `SunsetListDetailScaffold`

Lives at
`ui_common/.../component/designsystem/SunsetListDetailScaffold.kt`.
Wraps Material3 Adaptive's `ListDetailPaneScaffold(directive, value, ...)`
overload (the one that takes a `ThreePaneScaffoldValue` directly, not
the `scaffoldState` overload). Holds selection in a plain
`MutableState<T?>`, with `selectDetail(value)` and `back()` mutators.
Both `directive` (sizing) and the `ThreePaneScaffoldValue` (visibility)
are derived from the same selection state in the same recomposition, so
the layout flips atomically.

Visual modes:

| `state.selection` | `maxHorizontalPartitions` | Panes shown                |
|-------------------|---------------------------|----------------------------|
| `null`            | 1                         | List only, full width      |
| non-null          | default for width class   | List + Detail (two-pane)   |

We intentionally **do not** use
`rememberListDetailPaneScaffoldNavigator(scaffoldDirective = ...)` to
flip the directive based on selection. The navigator's `navigateTo` is
`suspend`, forcing the navigation through `coroutineScope.launch` while
the directive flip is synchronous — the resulting tear shows up on
real tablets as a single-pane Detail flash on tap, even though steady
states look correct in unit-level VRTs. See commit history for details.

If you ever need a back stack with multiple detail levels, reach for
the navigator-based `ListDetailPaneScaffold(directive, scaffoldState,
...)` overload directly inside `:ui_common` — but expose a new wrapper
shape rather than mixing both approaches in `SunsetListDetailScaffold`.

### Per-feature pane composable (`*PaneScreen`)

Detail screens that participate in the two-pane scaffold ship two
`Composable` entry points in the same file:

- The standalone `*Screen` (e.g. `AlbumScreen`, `ArtistScreen`,
  `TrackScreen`): used by the compact path's Nav3 push. Uses
  `SunsetBottomSheet` with a peek height computed from
  `LocalWindowInfo.containerSize` so it resembles a full-screen detail
  with a bottom sheet rising into the artwork area.
- The `*PaneScreen` (e.g. `AlbumPaneScreen`, `ArtistPaneScreen`,
  `TrackPaneScreen`): used as the scaffold's `detailPane`. Uses the
  same `SunsetBottomSheet` but with a fixed `sheetPeekHeight = 280.dp`
  and a translucent sheet container
  (`LocalAppTheme.current.backgroundColor().copy(alpha = 0.85f)`),
  because the scaffold's pane has a known fixed-ish width and shouldn't
  fight with screen-relative peek calculation.

Both variants accept `animatedVisibilityScope: AnimatedVisibilityScope`
(widened from `AnimatedContentScope`) so the same composable works
under both Nav3's `animatedContentScope` and the scaffold's
`AnimatedPane` scope.

### Shared element transition disabled in two-pane

Inside the scaffold path, pass `useSharedElement = false` to the inner
list `*Compact` composable, and pass `id = ""` to the `*PaneScreen` in
`detailPane`. The molecules (`Scrobble`, `TopAlbum`, `TopArtist`) and
`*PaneScreen` composables already gate `Modifier.sharedElement(...)` on
`id.isNotEmpty()`, so the empty `id` skips the transition cleanly. The
compact (Nav3 push) path keeps shared element transitions — they're
the right UX for full-screen detail navigation on phone, and they
visibly break inside a two-pane scaffold (artwork tries to animate
across pane boundaries).

### Currently adopted

- **Scrobble ↔ Track** (`feature/scrobble`): list-row tap →
  `TrackPaneScreen` in detail pane on tablet.
- **TopAlbums ↔ Album** (`feature/album`): grid tap →
  `AlbumPaneScreen`.
- **TopArtists ↔ Artist** (`feature/artist`): grid tap →
  `ArtistPaneScreen`.

Plumbing for the scaffold's `*ViewModelProvider` (e.g.
`albumViewModelProvider: @Composable (AlbumKey) -> AlbumViewModel`)
flows from `HomeNavigation` → `HomeScreen` → the `Top*Screen`. The
`*Navigation.kt` files keep their `destination<*Key>` registrations for
the compact Nav3 push path.

### Verifying tablet behavior

- Per-pane Roborazzi VRTs at `RobolectricDeviceQualifiers.PixelTablet`:
  `*_screen_tablet.png` for the standalone variant and
  `*_pane_screen_tablet.png` for the pane variant.
- Mechanical end-to-end: `LargeScreenSmokeTest` annotated
  `@LargeScreenE2E`, runs on the `pixelTabletApi35` Gradle Managed
  Device. See
  [`.claude/rules/e2e-testing.md`](.claude/rules/e2e-testing.md) for
  invocation.

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
- For list-then-detail flows that need to adapt to tablet, branch on
  `isCompactWidth()` and use `SunsetListDetailScaffold` for the
  expanded path. Ship a `*PaneScreen` variant alongside the standalone
  `*Screen` (fixed 280.dp peek + translucent sheet) and pass
  `useSharedElement = false` / `id = ""` inside the scaffold so shared
  element transitions stay disabled in two-pane mode.

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
- Don't add a new theme that needs its own `ColorScheme` without also
  adding a dedicated `*Color` object alongside `DarkColor` / `LightColor` /
  `LastFmDarkColor` and updating `accentColor()` + `colorScheme()` in
  `SunsetTheme.kt`. (`FOLLOW_SYSTEM` is the exception — it intentionally
  has no scheme of its own and resolves to `DARK`/`LIGHT` at composition
  time via `AppTheme.resolve(...)`.)
- Don't import `androidx.compose.material3.adaptive.*` outside
  `:ui_common`. Consume `SunsetListDetailScaffold`,
  `rememberSunsetListDetailScaffoldState`, and `isCompactWidth()`
  instead. Enforced by the `PreferSunsetListDetailPaneScaffold` lint
  detector.
- Don't pass `scaffoldDirective` to
  `rememberListDetailPaneScaffoldNavigator` to flip
  `maxHorizontalPartitions` based on selection — the navigator's
  `navigateTo` is `suspend` and the resulting async-vs-sync split
  produces a single-pane Detail flash on tap. Use the `(directive,
  value, ...)` overload via `SunsetListDetailScaffold` instead.

## Authentication (Last.fm web auth)

The app never sees the user's Last.fm password. `LoginScreen` shows a single "Sign in with Last.fm" button; tapping it opens `https://www.last.fm/api/auth/?api_key=…&cb=https://sunsetscrob.mataku.com/auth/lastfm` in a Chrome Auth Tab (`androidx.browser` 1.10.0, Chrome 137+). Last.fm redirects to the callback with `?token=…`, the Auth Tab returns the URI through an `ActivityResultLauncher`, and `LoginViewModel.authorize(token)` exchanges it via `auth.getSession` (`SessionRepository.authorize`).

Two delivery paths converge on the same ViewModel entry point:

| Path | When | Mechanism |
|------|------|-----------|
| Auth Tab result | Chrome 137+ | `AuthTabWebAuthLauncher.rememberLaunch` → `LoginViewModel.onWebAuthResult` |
| App Link fallback | Browser without Auth Tab support (falls back to a plain Custom Tab) | `MainActivity` (`singleTask`, `onNewIntent`) parses the intent with `LastFmWebAuth.tokenFromCallback` and pushes the token into `WebAuthCallbackChannel`, which `LoginViewModel` collects |

`WebAuthCallbackChannel` is a conflated, consume-once channel so a token is exchanged exactly once even if the Activity is recreated. `MainActivity` only reads the launch intent when `savedInstanceState == null` for the same reason.

The callback host/path (`sunsetscrob.mataku.com/auth/lastfm`) is a contract with the Cloudflare Worker in the `sunsetscrob.mataku.com` repository, which serves `/.well-known/assetlinks.json` for both `com.mataku.scrobscrob` and `com.mataku.scrobscrob.dev`. The `cb` URL and the `data` element of the manifest intent-filter must change together with that repository.

`LastFmWebAuthLauncher` is the test seam: the E2E graph excludes `AuthModule` and binds a fake that returns a fixed token synchronously, so instrumentation tests never open a browser or reach the network. See `.claude/rules/e2e-testing.md`.

`SessionRepository.webAuthUrl()` is a `Flow<String>` rather than a plain accessor because the API key lives in `:data:api`'s `BuildConfig`, which feature modules may not import, and the `RepositoryReturnsFlow` Lint rule keeps repository interfaces Flow-shaped.
