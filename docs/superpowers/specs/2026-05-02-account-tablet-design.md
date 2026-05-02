# Account screen — tablet two-pane design

Status: Draft (awaiting implementation plan)
Branch: `feature/optimize-tablet`
Related working doc: `tablet_device_optimization.md` (Phase 3 — Account)

## Goal

On expanded width, render the Account screen as a list-detail layout where:

- The list pane shows the existing Account menu (profile + settings rows).
- The detail pane shows whichever sub-screen the user selected: Theme, Licenses,
  Privacy Policy, or Scrobble Setting.
- When no detail is selected, the scaffold collapses to a single pane and Account
  fills the full width — the same behaviour `SunsetListDetailScaffold` already
  provides via `maxHorizontalPartitions = 1`.

On compact width, the screen behaves exactly as today: each row pushes via
Nav3 to its own destination.

## Non-goals

- The debug-only **UI Catalog** entry stays as a separate Activity launched via
  `Intent` (`AppInfoProvider.navigateToUiCatalogIntent`). It is **not** a detail
  pane candidate.
- **Logout / Clear cache / App update** stay as dialogs / Play Core flows on
  both compact and expanded — they are not screens.
- Selection persistence across configuration change is **out of scope**.
  `rememberSunsetListDetailScaffoldState` does not use `rememberSaveable`
  today; matching that behaviour keeps Account consistent with `ScrobbleScreen`
  and `TopAlbumsScreen`.
- New E2E tests. VRT covers the layout. The existing `LargeScreenSmokeTest`
  already exercises the scaffold path generically.

## Architecture

All changes live in `:feature:account`.

| File | Change |
| --- | --- |
| `account/ui/screen/AccountScreen.kt` | Add `isCompactWidth()` branch. Expanded path wraps in `SunsetListDetailScaffold<AccountDetail>`. Add `internal sealed interface AccountDetail` at file end. |
| `account/ui/navigation/AccountNavigation.kt` | In `destination<AccountKey>`, build three `@Composable () -> X` ViewModel providers and pass them to `AccountScreen`. The standalone `destination<*Key>` blocks for the four sub-screens stay registered for the compact path. |
| `account/src/test/.../AccountScreenTest.kt` | Update `layout_tablet` (selection-empty, list-only collapse). Add tablet VRTs for Theme-selected and License-selected states. |

Module dependencies do not change. No new convention plugin or Konsist spec.

## Components

### `AccountDetail` selection type

```kotlin
internal sealed interface AccountDetail {
  data object Theme : AccountDetail
  data object License : AccountDetail
  data object PrivacyPolicy : AccountDetail
  data object ScrobbleSetting : AccountDetail
}
```

- Lives at the bottom of `AccountScreen.kt`. Not a `SunsetNavKey` — selection
  is scaffold-local, not graph-routable. No `@Serializable` / `@Immutable`
  needed beyond what Kotlin gives `data object` for free.
- `data object`s are `distinct` so they work as Material3 Adaptive content keys
  via `SunsetListDetailScaffold`'s existing `contentKey = state.selection`.

### `AccountScreen` signature

Adds three provider parameters. `PrivacyPolicyScreen` has no ViewModel today,
so no provider is needed for it.

```kotlin
@Composable
internal fun AccountScreen(
  viewModel: AccountViewModel,
  themeSelectorViewModelProvider: @Composable () -> ThemeSelectorViewModel,
  licenseViewModelProvider: @Composable () -> LicenseViewModel,
  scrobbleSettingViewModelProvider: @Composable () -> ScrobbleSettingViewModel,
  showPermissionHelp: () -> Unit,
  navigateToScrobbleSetting: () -> Unit,
  navigateToPrivacyPolicy: () -> Unit,
  navigateToThemeSelector: () -> Unit,
  navigateToLicenseList: () -> Unit,
  navigateToLogin: () -> Unit,
  modifier: Modifier = Modifier,
)
```

**Provider invocation rule**: providers must be called only inside the
`detailPane` `when (selection)` branch that consumes them. Calling them
eagerly at the top of `AccountScreen` would create the underlying VM
immediately and run its `init` — defeating the lazy semantics that the
provider lambda exists to preserve. The pattern matches
`ScrobbleScreen.trackViewModelProvider(selection)`.

### Tablet branch shape

The notification-permission gate is **already inside `AccountContent`** —
the `scrobble` row's tap handler checks
`NotificationManagerCompat.getEnabledListenerPackages(context)` itself and
only invokes the `navigateToScrobbleSetting` callback when permission is
already granted (see existing `AccountScreen.kt`). The `navigateToScrobbleSetting`
callback we pass into `AccountContent` therefore only needs to do one thing
per form factor: push (compact) or swap detail (expanded).

The launcher (`notificationPermissionLauncher`) fires when the user returns
from the system notification-listener settings. It needs to read the same
form-factor decision, so `scaffoldState` and `compact` must be accessible
from outside the branch — hoist them to the top of `AccountScreen`.

```kotlin
val compact = isCompactWidth()
val scaffoldState = rememberSunsetListDetailScaffoldState<AccountDetail>()

val notificationPermissionLauncher =
  rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (NotificationManagerCompat.getEnabledListenerPackages(context)
        .contains(context.packageName)
    ) {
      if (compact) {
        navigateToScrobbleSetting.invoke()
      } else {
        scaffoldState.selectDetail(AccountDetail.ScrobbleSetting)
      }
    }
  }

val theme = uiState.theme ?: return@SunsetScaffold
if (compact) {
  AccountContent(
    theme = theme,
    navigateToThemeSelector = navigateToThemeSelector,
    navigateToLicenseList = navigateToLicenseList,
    navigateToPrivacyPolicy = navigateToPrivacyPolicy,
    navigateToScrobbleSetting = navigateToScrobbleSetting,
    // ... existing dialogs / app-update / clear-cache callbacks
  )
} else {
  SunsetListDetailScaffold(
    state = scaffoldState,
    listPane = {
      AccountContent(
        theme = theme,
        navigateToThemeSelector = { scaffoldState.selectDetail(AccountDetail.Theme) },
        navigateToLicenseList = { scaffoldState.selectDetail(AccountDetail.License) },
        navigateToPrivacyPolicy = { scaffoldState.selectDetail(AccountDetail.PrivacyPolicy) },
        navigateToScrobbleSetting = { scaffoldState.selectDetail(AccountDetail.ScrobbleSetting) },
        // navigateToUiCatalog, logout, app update, clear cache, navigateToNotificationSetting unchanged
      )
    },
    detailPane = { selection ->
      when (selection) {
        AccountDetail.Theme -> ThemeSelectorScreen(
          viewModel = themeSelectorViewModelProvider(),
          onBackPressed = { scaffoldState.back() },
        )
        AccountDetail.License -> LicenseScreen(
          viewModel = licenseViewModelProvider(),
          onBackPressed = { scaffoldState.back() },
        )
        AccountDetail.PrivacyPolicy -> PrivacyPolicyScreen(
          onBackPressed = { scaffoldState.back() },
        )
        AccountDetail.ScrobbleSetting -> ScrobbleSettingScreen(
          viewModel = scrobbleSettingViewModelProvider(),
          onBackPressed = { scaffoldState.back() },
        )
        null -> Unit
      }
    },
  )
}
```

`rememberSunsetListDetailScaffoldState<AccountDetail>()` is hoisted
unconditionally at the top of `AccountScreen` (rather than inside the
`else` branch) so that `remember` slots stay stable when `isCompactWidth()`
flips at runtime (window resize on foldables, configuration change).
The state is harmless when unused on the compact path — it is just a
`MutableState<AccountDetail?>` initialised to `null`.

## Compact vs expanded matrix

| Scenario | Compact (phone) | Expanded (tablet) |
| --- | --- | --- |
| Initial render | Account only | Account only (scaffold collapsed to 1 partition) |
| Tap Theme | `navigate(ThemeSelectorKey)` push | `selectDetail(Theme)` → 2 panes |
| Tap another row while a detail is open | New entry pushed onto stack | Selection swaps; no stack growth |
| Back press with detail open | `popBackStack()` to Account | `state.back()` clears selection; stack untouched |
| Back press with no detail open | Pop Account | Pop Account |
| Logout / Clear cache / App update | Dialog or Play Core flow in list pane | Same — list pane only |
| UI Catalog (debug build) | Intent to standalone Activity | Same |
| Notification permission grant | Launcher → `navigateToScrobbleSetting()` | Launcher → `selectDetail(ScrobbleSetting)` |

## ViewModel lifetime

`viewModelFor<X>(key)` resolves to
`viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current, key = key.toString())`,
and `LocalViewModelStoreOwner` is the host Activity (see the implementation
note in `SunsetNavHost.viewModelFor`, lines 123–154). Implications for this
design:

- A detail VM created on tablet (e.g. `ThemeSelectorViewModel` keyed by
  `ThemeSelectorKey`) shares the same instance with the compact-path
  `destination<ThemeSelectorKey>` block, because both call
  `viewModelFor<ThemeSelectorViewModel>(ThemeSelectorKey)`. This is intentional
  — switching form factors mid-process is rare, and any divergence between the
  two would be more surprising than reuse.
- The detail VM survives `state.back()` until the host Activity is destroyed.
  Re-selecting Theme after deselecting reuses the VM and does **not** re-run
  `init`. All four candidate VMs observe Flows reactively, so reuse is benign.
- Provider lambdas `{ viewModelFor<X>(Key) }` are inert until invoked. Calling
  them inside the `when (selection)` branch is what gates VM creation on actual
  selection.

## AccountNavigation changes

```kotlin
fun SunsetNavBuilder.accountGraph() {
  destination<AccountKey> { key ->
    val context = LocalContext.current
    AccountScreen(
      viewModel = viewModelFor<AccountViewModel>(key),
      themeSelectorViewModelProvider = {
        viewModelFor<ThemeSelectorViewModel>(ThemeSelectorKey)
      },
      licenseViewModelProvider = {
        viewModelFor<LicenseViewModel>(LicenseKey)
      },
      scrobbleSettingViewModelProvider = {
        viewModelFor<ScrobbleSettingViewModel>(ScrobbleSettingKey)
      },
      navigateToScrobbleSetting = { navigate(ScrobbleSettingKey) },
      navigateToThemeSelector = { navigate(ThemeSelectorKey) },
      navigateToLicenseList = { navigate(LicenseKey) },
      navigateToPrivacyPolicy = { navigate(PrivacyPolicyKey) },
      navigateToLogin = { /* existing */ },
      showPermissionHelp = { /* existing Toast */ },
    )
  }
  // ScrobbleSettingKey / ThemeSelectorKey / LicenseKey / PrivacyPolicyKey
  // destination blocks remain unchanged — they are still used on the compact path.
}
```

## Testing

### VRT updates (`AccountScreenTest`)

| Test | Device | State | File |
| --- | --- | --- | --- |
| `layout_tablet` (modified) | `PixelTablet` | No selection — list-only collapse | `account_screen_tablet.png` (re-record) |
| `layout_tablet_theme_selected` (new) | `PixelTablet` | Theme row tapped before capture | `account_screen_tablet_theme.png` |
| `layout_tablet_license_selected` (new) | `PixelTablet` | Licenses row tapped before capture | `account_screen_tablet_license.png` |

The new tests reuse the existing mock setup (`themeRepository.currentTheme()`
already returns `flowOf(AppTheme.DARK)`; for License the test will need to add
`licenseRepository.licenseList()` returning a tiny canned list). Selection is
driven via `actionsBeforeCapturing` performing `onNodeWithText("Theme").performClick()`.

`ScrobbleSettingScreen` and `PrivacyPolicyScreen` detail variants are
intentionally **not** captured: the former routes through the notification
permission launcher (untestable in Roborazzi without elaborate mocking),
the latter renders a `WebView` whose paint isn't captured deterministically.
Compact VRT for both already exists.

### Unit tests

`AccountViewModel`, `ThemeSelectorViewModel`, `LicenseViewModel`,
`ScrobbleSettingViewModel`, and their existing `*Spec` files do not change.

### Konsist / Lint

- `PreferSunsetListDetailPaneScaffoldDetector` permits the wrapper. No
  direct `androidx.compose.material3.adaptive.*` imports are introduced.
- `:architecture-spec:test` does not need a new sensor — the change does
  not introduce a new layering rule.

## Risks and rollback

- **Risk**: If `AccountContent` is invoked from inside `listPane`, the
  notification-permission dialog and the clear-cache confirmation dialog
  render only over the list pane (their `if (open) Dialog(...)` branches
  live inside `AccountContent`). Visually that is fine because dialogs are
  modal overlays anyway, but it is worth a manual smoke check. If it
  proves wrong, lift the dialog state into `AccountScreen` and render
  it outside the scaffold.
- **Rollback**: Revert the `AccountScreen.kt` and `AccountNavigation.kt`
  edits and the new VRT files. No data migration, no DI graph change.

## Open questions

None — pending implementation plan only.
