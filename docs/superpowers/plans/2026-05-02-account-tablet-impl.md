# Account tablet two-pane Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Render the Account screen as a list-detail layout on expanded width while leaving the compact (phone) experience unchanged.

**Architecture:** Wrap the existing `AccountContent` in `SunsetListDetailScaffold<AccountDetail>` only when `isCompactWidth()` is false. The scaffold collapses to a single pane when nothing is selected, so the "Account fills the screen when no detail is open" requirement falls out for free. ViewModels for the four detail screens (`ThemeSelectorViewModel`, `LicenseViewModel`, `ScrobbleSettingViewModel`; `PrivacyPolicyScreen` has no VM) are passed as `@Composable () -> X` providers from `accountGraph()` and invoked lazily inside the `detailPane` `when` branch so their `init` blocks only run when the user actually selects a detail.

**Tech Stack:** Kotlin / Jetpack Compose / Material3 Adaptive (`SunsetListDetailScaffold` wrapper) / Metro DI (`viewModelFor` + Activity-scoped `ViewModelStore`) / Roborazzi VRT.

**Spec:** `docs/superpowers/specs/2026-05-02-account-tablet-design.md` (commit `e326f975`).

**Branch:** `feature/optimize-tablet`. All changes confined to `:feature:account`.

---

## File Structure

| File | Status | Responsibility |
| --- | --- | --- |
| `feature/account/src/main/java/com/mataku/scrobscrob/account/ui/screen/AccountScreen.kt` | Modify | Add `AccountDetail` sealed interface; add three provider params; branch on `isCompactWidth()`; hoist `scaffoldState` and rewire the notification-permission launcher. |
| `feature/account/src/main/java/com/mataku/scrobscrob/account/ui/navigation/AccountNavigation.kt` | Modify | Build the three providers in `destination<AccountKey>` and pass them to `AccountScreen`. The four sub-key destinations stay registered for the compact path. |
| `feature/account/src/test/java/com/mataku/scrobscrob/account/ui/screen/AccountScreenTest.kt` | Modify | Update existing tests for the new signature; add `layout_tablet_theme_selected` and `layout_tablet_license_selected`. |
| `feature/account/screenshot/account_screen_tablet.png` | Re-record | Likely visually identical (no selection → list-only collapse), but re-record to prove it. |
| `feature/account/screenshot/account_screen_tablet_theme.png` | Create | New tablet golden — Theme selected, two panes. |
| `feature/account/screenshot/account_screen_tablet_license.png` | Create | New tablet golden — Licenses selected, two panes. |

No new modules, convention plugins, Konsist specs, or Lint detectors.

---

## Task 1: Add `AccountDetail` sealed interface

**Files:**
- Modify: `feature/account/src/main/java/com/mataku/scrobscrob/account/ui/screen/AccountScreen.kt`

- [ ] **Step 1: Append the sealed interface to the bottom of `AccountScreen.kt`**

Add at the end of the file (after `AccountContentPreview`):

```kotlin
internal sealed interface AccountDetail {
  data object Theme : AccountDetail
  data object License : AccountDetail
  data object PrivacyPolicy : AccountDetail
  data object ScrobbleSetting : AccountDetail
}
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :feature:account:compileDebugKotlin`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 3: Commit**

```bash
git add feature/account/src/main/java/com/mataku/scrobscrob/account/ui/screen/AccountScreen.kt
git commit -m "$(cat <<'EOF'
feat(account): add AccountDetail sealed interface for tablet selection

Internal type used by SunsetListDetailScaffold on expanded width to
identify which sub-screen (Theme / License / PrivacyPolicy /
ScrobbleSetting) is currently rendered in the detail pane. Not a
SunsetNavKey because selection is scaffold-local, not graph-routable.

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

## Task 2: Extend `AccountScreen` signature with detail-VM providers and thread them through `accountGraph()`

This task is purely additive: the new parameters are not consumed yet, so behavior on both compact and expanded width is unchanged. The existing tablet golden continues to match.

**Files:**
- Modify: `feature/account/src/main/java/com/mataku/scrobscrob/account/ui/screen/AccountScreen.kt`
- Modify: `feature/account/src/main/java/com/mataku/scrobscrob/account/ui/navigation/AccountNavigation.kt`
- Modify: `feature/account/src/test/java/com/mataku/scrobscrob/account/ui/screen/AccountScreenTest.kt`

- [ ] **Step 1: Add provider parameters to `AccountScreen`**

Edit `AccountScreen.kt`. Add the three import lines:

```kotlin
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
```

Change the signature from:

```kotlin
@Composable
internal fun AccountScreen(
  viewModel: AccountViewModel,
  showPermissionHelp: () -> Unit,
  navigateToScrobbleSetting: () -> Unit,
  navigateToPrivacyPolicy: () -> Unit,
  navigateToThemeSelector: () -> Unit,
  navigateToLicenseList: () -> Unit,
  navigateToLogin: () -> Unit,
  modifier: Modifier = Modifier
)
```

to:

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
  modifier: Modifier = Modifier
)
```

Do not consume the new parameters in the body yet. The body remains exactly as today.

- [ ] **Step 2: Update `accountGraph()` to construct the providers**

Edit `AccountNavigation.kt`. Replace the `destination<AccountKey>` block with:

```kotlin
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
    navigateToLogin = { /* 認証 gate 切替で処理、ここでは何もしない */ },
    navigateToPrivacyPolicy = { navigate(PrivacyPolicyKey) },
    showPermissionHelp = {
      Toast.makeText(
        context.applicationContext,
        R.string.label_notification_permission_help,
        Toast.LENGTH_LONG,
      ).show()
    },
    modifier = Modifier,
  )
}
```

Do not change the four other `destination<*Key>` blocks below — they stay registered for the compact path.

- [ ] **Step 3: Update `AccountScreenTest` to pass mocked providers**

Edit `AccountScreenTest.kt`. For each of the three test methods (`layout`, `layout_light`, `layout_tablet`) update the `AccountScreen(...)` call site by adding the three provider arguments **before** the existing `showPermissionHelp` argument:

```kotlin
AccountScreen(
  viewModel = viewModel,
  themeSelectorViewModelProvider = { mockk() },
  licenseViewModelProvider = { mockk() },
  scrobbleSettingViewModelProvider = { mockk() },
  showPermissionHelp = {},
  navigateToLogin = mockk(),
  navigateToPrivacyPolicy = mockk(),
  navigateToScrobbleSetting = mockk(),
  navigateToLicenseList = mockk(),
  navigateToThemeSelector = mockk()
)
```

The existing `import io.mockk.mockk` already covers the new lambdas. Returning a bare `mockk()` is acceptable here because the providers are not invoked in any of these three tests (no detail is selected, and Task 3's expanded branch — which is the only caller — does not exist yet).

- [ ] **Step 4: Verify the unit/test bucket builds and the screenshot bucket still passes**

Run: `./gradlew :feature:account:testDebugUnitTest -PexcludeScreenshotTest=true`
Expected: BUILD SUCCESSFUL, all existing specs pass.

Run: `./gradlew :feature:account:verifyRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true`
Expected: BUILD SUCCESSFUL — `account_screen.png`, `account_screen_light.png`, and `account_screen_tablet.png` match their goldens unchanged because the screen body is byte-identical.

- [ ] **Step 5: Commit**

```bash
git add feature/account/src/main/java/com/mataku/scrobscrob/account/ui/screen/AccountScreen.kt \
        feature/account/src/main/java/com/mataku/scrobscrob/account/ui/navigation/AccountNavigation.kt \
        feature/account/src/test/java/com/mataku/scrobscrob/account/ui/screen/AccountScreenTest.kt
git commit -m "$(cat <<'EOF'
feat(account): thread detail-VM providers into AccountScreen

Add themeSelectorViewModelProvider, licenseViewModelProvider, and
scrobbleSettingViewModelProvider parameters to AccountScreen and have
accountGraph() build them with viewModelFor<X>(<sub-key>). The
parameters are not consumed yet — Task 3 wires them into the expanded
branch. Existing tests updated to pass mockk() providers.

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

## Task 3: Implement the expanded branch with `SunsetListDetailScaffold`

**Files:**
- Modify: `feature/account/src/main/java/com/mataku/scrobscrob/account/ui/screen/AccountScreen.kt`

- [ ] **Step 1: Add the new imports**

Add to the top of `AccountScreen.kt`:

```kotlin
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetListDetailScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.rememberSunsetListDetailScaffoldState
import com.mataku.scrobscrob.ui_common.style.isCompactWidth
```

(The first three may already be present from Task 2 — keep them deduplicated.)

- [ ] **Step 2: Hoist `compact` and `scaffoldState` above the existing `SunsetScaffold` block**

Inside `AccountScreen`, immediately after the `val uiState by viewModel.uiState.collectAsStateWithLifecycle()` line, insert:

```kotlin
val compact = isCompactWidth()
val scaffoldState = rememberSunsetListDetailScaffoldState<AccountDetail>()
```

This puts the `remember` slot on a stable code path so the selection survives recompositions even when the window changes form factor mid-process (foldables, multi-window).

- [ ] **Step 3: Rewire `notificationPermissionLauncher` to honor the form factor**

Replace the existing launcher block:

```kotlin
val notificationPermissionLauncher =
  rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (NotificationManagerCompat.getEnabledListenerPackages(context)
        .contains(context.packageName)
    ) {
      navigateToScrobbleSetting.invoke()
    }
  }
```

with:

```kotlin
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
```

- [ ] **Step 4: Extract a private `AccountListPane` helper at the bottom of the file**

The current `AccountScreen` body has a single `SunsetScaffold(topBar = "Account")` wrapping `AccountContent`. We need this list-pane structure (topBar + AccountContent) to render in two places: as the entire screen on compact, and inside `SunsetListDetailScaffold.listPane` on expanded. Duplicating ~30 lines is noisy, and wrapping the `if (compact) / else` in a single outer `SunsetScaffold(topBar = "Account")` is wrong — each detail screen brings its own `SunsetScaffold` with its own topBar (Theme / Licenses / Privacy Policy / Scrobble Setting), so an outer "Account" topBar would stack two app bars vertically in the expanded layout.

Add this private composable at the bottom of `AccountScreen.kt`, just before `AccountContentPreview`:

```kotlin
@Composable
private fun AccountListPane(
  theme: AppTheme,
  appVersion: String,
  appUpdateInfo: AppUpdateInfo?,
  imageCacheMB: String?,
  userInfo: UserInfo?,
  onTapTheme: () -> Unit,
  onTapLicense: () -> Unit,
  onTapPrivacyPolicy: () -> Unit,
  onTapScrobbleSetting: () -> Unit,
  onTapLogout: () -> Unit,
  onTapNotificationSetting: () -> Unit,
  onRequestAppUpdate: (AppUpdateInfo) -> Unit,
  onClearCache: () -> Unit,
  onNavigateToUiCatalog: () -> Unit,
  modifier: Modifier = Modifier,
) {
  SunsetScaffold(
    modifier = modifier,
    topBar = {
      SunsetTopAppBar(
        title = {
          SunsetText.Title(text = "Account")
        },
      )
    }
  ) { paddingValues ->
    AccountContent(
      theme = theme,
      navigateToThemeSelector = onTapTheme,
      navigateToLogoutConfirmation = onTapLogout,
      navigateToLicenseList = onTapLicense,
      navigateToPrivacyPolicy = onTapPrivacyPolicy,
      navigateToScrobbleSetting = onTapScrobbleSetting,
      navigateToNotificationSetting = onTapNotificationSetting,
      appUpdateInfo = appUpdateInfo,
      requestAppUpdate = onRequestAppUpdate,
      appVersion = appVersion,
      clearCache = onClearCache,
      navigateToUiCatalog = onNavigateToUiCatalog,
      imageCacheMB = imageCacheMB,
      userInfo = userInfo,
      modifier = Modifier.padding(paddingValues)
    )
  }
}
```

`AccountContent` itself is unchanged — only its caller is refactored.

- [ ] **Step 5: Replace `AccountScreen`'s scaffold body with the compact/expanded branch**

Replace the existing block:

```kotlin
uiState.theme?.let {
  SunsetScaffold(
    modifier = modifier,
    topBar = { /* "Account" topBar */ },
  ) { paddingValues ->
    AccountContent(
      theme = it,
      /* … many params … */
      modifier = Modifier.padding(paddingValues)
    )
  }
}
```

with:

```kotlin
uiState.theme?.let { theme ->
  val onTapLogout: () -> Unit = { openDialog.value = true }
  val onTapNotificationSetting: () -> Unit = {
    val intent = Intent().apply {
      action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
    }
    notificationPermissionLauncher.launch(intent)
    showPermissionHelp.invoke()
  }
  val onRequestAppUpdate: (AppUpdateInfo) -> Unit = { appUpdateInfo ->
    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
      viewModel.completeUpdate()
    } else {
      coroutineScope.launch {
        kotlin.runCatching {
          val updateInfo = appUpdateManager.requestAppUpdateInfo()
          appUpdateManager.startUpdateFlowForResult(
            updateInfo,
            context as Activity,
            AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE),
            1
          )
          appUpdateLauncher.launch(context.intent)
        }
      }
    }
  }

  if (compact) {
    AccountListPane(
      theme = theme,
      appVersion = uiState.appVersion,
      appUpdateInfo = uiState.appUpdateInfo,
      imageCacheMB = uiState.imageCacheMB,
      userInfo = uiState.userInfo,
      onTapTheme = navigateToThemeSelector,
      onTapLicense = navigateToLicenseList,
      onTapPrivacyPolicy = navigateToPrivacyPolicy,
      onTapScrobbleSetting = navigateToScrobbleSetting,
      onTapLogout = onTapLogout,
      onTapNotificationSetting = onTapNotificationSetting,
      onRequestAppUpdate = onRequestAppUpdate,
      onClearCache = viewModel::clearCache,
      onNavigateToUiCatalog = viewModel::navigateToUiCatalog,
      modifier = modifier,
    )
  } else {
    SunsetListDetailScaffold(
      state = scaffoldState,
      modifier = modifier,
      listPane = {
        AccountListPane(
          theme = theme,
          appVersion = uiState.appVersion,
          appUpdateInfo = uiState.appUpdateInfo,
          imageCacheMB = uiState.imageCacheMB,
          userInfo = uiState.userInfo,
          onTapTheme = { scaffoldState.selectDetail(AccountDetail.Theme) },
          onTapLicense = { scaffoldState.selectDetail(AccountDetail.License) },
          onTapPrivacyPolicy = { scaffoldState.selectDetail(AccountDetail.PrivacyPolicy) },
          onTapScrobbleSetting = { scaffoldState.selectDetail(AccountDetail.ScrobbleSetting) },
          onTapLogout = onTapLogout,
          onTapNotificationSetting = onTapNotificationSetting,
          onRequestAppUpdate = onRequestAppUpdate,
          onClearCache = viewModel::clearCache,
          onNavigateToUiCatalog = viewModel::navigateToUiCatalog,
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
}
```

The three local lambdas (`onTapLogout`, `onTapNotificationSetting`, `onRequestAppUpdate`) are extracted so both branches share them and the call sites stay readable. They are not `remember`-ed because they capture only stable properties (`openDialog`, the launcher, the VM, the scope) that are themselves stable across recompositions.

- [ ] **Step 6: Verify it compiles**

Run: `./gradlew :feature:account:compileDebugKotlin`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 7: Run unit tests (no VM behavior changes, should pass)**

Run: `./gradlew :feature:account:testDebugUnitTest -PexcludeScreenshotTest=true`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 8: Verify the existing tablet golden against the new layout**

Run: `./gradlew :feature:account:verifyRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true`
Expected: Either PASS (the collapsed scaffold renders byte-identically to today's full-width AccountContent) or FAIL on `account_screen_tablet.png` only. `account_screen.png` and `account_screen_light.png` must continue to pass — they exercise the compact path and must not change.

- [ ] **Step 9: If verify failed on the tablet golden, re-record it**

Run: `./gradlew :feature:account:recordRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true`

Open `feature/account/screenshot/account_screen_tablet.png` and visually confirm:
- Layout matches today's `account_screen_tablet.png` (the spec working doc references this image as the "edge-to-edge stretched rows" baseline).
- No detail pane is visible; the list (Account menu) fills the screen.

- [ ] **Step 10: Commit**

```bash
git add feature/account/src/main/java/com/mataku/scrobscrob/account/ui/screen/AccountScreen.kt
# Only stage the golden if it actually changed:
git status feature/account/screenshot/
# If account_screen_tablet.png shows up as modified:
git add feature/account/screenshot/account_screen_tablet.png

git commit -m "$(cat <<'EOF'
feat(account): render expanded width as list-detail scaffold

Wrap AccountContent in SunsetListDetailScaffold<AccountDetail> when
isCompactWidth() is false. With no detail selected the scaffold
collapses to a single pane, so Account fills the full width. Selecting
a row in the list (Theme / Licenses / Privacy Policy / Scrobble
Setting) swaps the detail pane in place rather than pushing onto the
back stack. The notification-permission launcher now branches on form
factor: compact pushes via navigate(ScrobbleSettingKey); expanded
swaps the scaffold detail.

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

## Task 4: Add `layout_tablet_theme_selected` VRT

**Files:**
- Modify: `feature/account/src/test/java/com/mataku/scrobscrob/account/ui/screen/AccountScreenTest.kt`
- Create: `feature/account/screenshot/account_screen_tablet_theme.png`

- [ ] **Step 1: Add the test method**

Add these imports if not present:

```kotlin
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import kotlinx.coroutines.flow.MutableStateFlow
```

Append to `AccountScreenTest`:

```kotlin
@Test
fun layout_tablet_theme_selected() {
  val viewModel = AccountViewModel(
    usernameRepository,
    themeRepository,
    sessionRepository,
    appInfoProvider,
    appUpdateManager,
    fileRepository,
    application,
    userRepository,
  )
  val themeSelectorViewModel = mockk<ThemeSelectorViewModel> {
    every { uiState } returns MutableStateFlow(
      ThemeSelectorViewModel.ThemeSelectorUiState(
        theme = AppTheme.DARK,
        event = null,
      )
    )
  }
  composeTestRule.captureScreenshot(
    device = RobolectricDeviceQualifiers.PixelTablet,
    appTheme = AppTheme.DARK,
    content = {
      AccountScreen(
        viewModel = viewModel,
        themeSelectorViewModelProvider = { themeSelectorViewModel },
        licenseViewModelProvider = { mockk() },
        scrobbleSettingViewModelProvider = { mockk() },
        showPermissionHelp = {},
        navigateToLogin = mockk(),
        navigateToPrivacyPolicy = mockk(),
        navigateToScrobbleSetting = mockk(),
        navigateToLicenseList = mockk(),
        navigateToThemeSelector = mockk()
      )
    },
    actionsBeforeCapturing = {
      composeTestRule.onNodeWithText("Theme").performClick()
      composeTestRule.waitForIdle()
    },
    fileName = "account_screen_tablet_theme.png"
  )
}
```

The Theme menu cell title is sourced from `AccountMenu.THEME.titleRes` which resolves to "Theme" (English string resource). `onNodeWithText("Theme")` matches it.

- [ ] **Step 2: Record the new golden**

Run: `./gradlew :feature:account:recordRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true`

This records all VRTs in the bucket. The new file appears at `feature/account/screenshot/account_screen_tablet_theme.png`.

- [ ] **Step 3: Visually inspect the new golden**

Open `feature/account/screenshot/account_screen_tablet_theme.png`. Confirm:
- Two panes are visible side by side.
- Left pane: Account menu (the existing list).
- Right pane: Theme selector with theme rows (Dark / Light / Follow system / etc.).

- [ ] **Step 4: Run verify to confirm the recorded image matches**

Run: `./gradlew :feature:account:verifyRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 5: Commit**

```bash
git add feature/account/src/test/java/com/mataku/scrobscrob/account/ui/screen/AccountScreenTest.kt \
        feature/account/screenshot/account_screen_tablet_theme.png
git commit -m "$(cat <<'EOF'
test(account): add tablet VRT for Theme detail pane

Drives the scaffold by performing a click on the "Theme" row before
capture so the right pane renders ThemeSelectorScreen alongside the
list. Theme VM is provided as a stubbed mockk; the live AccountVM is
kept (matches the existing tablet VRT setup).

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

## Task 5: Add `layout_tablet_license_selected` VRT

**Files:**
- Modify: `feature/account/src/test/java/com/mataku/scrobscrob/account/ui/screen/AccountScreenTest.kt`
- Create: `feature/account/screenshot/account_screen_tablet_license.png`

- [ ] **Step 1: Add the test method**

Add these imports if not present:

```kotlin
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.core.entity.LicenseArtifact
import com.mataku.scrobscrob.core.entity.SpdxLicense
import kotlinx.collections.immutable.persistentListOf
```

Append to `AccountScreenTest`:

```kotlin
@Test
fun layout_tablet_license_selected() {
  val viewModel = AccountViewModel(
    usernameRepository,
    themeRepository,
    sessionRepository,
    appInfoProvider,
    appUpdateManager,
    fileRepository,
    application,
    userRepository,
  )
  val licenseViewModel = mockk<LicenseViewModel> {
    every { uiState } returns MutableStateFlow(
      LicenseViewModel.LicenseUiState(
        licenseList = persistentListOf(
          LicenseArtifact(
            artifactId = "compose-runtime",
            groupId = "androidx.compose.runtime",
            name = "Compose Runtime",
            scm = null,
            spdxLicenses = persistentListOf(
              SpdxLicense(
                identifier = "Apache-2.0",
                name = "Apache License 2.0",
                url = "https://www.apache.org/licenses/LICENSE-2.0",
              )
            ),
            version = "1.7.0",
          ),
        )
      )
    )
  }
  composeTestRule.captureScreenshot(
    device = RobolectricDeviceQualifiers.PixelTablet,
    appTheme = AppTheme.DARK,
    content = {
      AccountScreen(
        viewModel = viewModel,
        themeSelectorViewModelProvider = { mockk() },
        licenseViewModelProvider = { licenseViewModel },
        scrobbleSettingViewModelProvider = { mockk() },
        showPermissionHelp = {},
        navigateToLogin = mockk(),
        navigateToPrivacyPolicy = mockk(),
        navigateToScrobbleSetting = mockk(),
        navigateToLicenseList = mockk(),
        navigateToThemeSelector = mockk()
      )
    },
    actionsBeforeCapturing = {
      composeTestRule.onNodeWithText("Licenses").performClick()
      composeTestRule.waitForIdle()
    },
    fileName = "account_screen_tablet_license.png"
  )
}
```

`AccountMenu.LICENSE.titleRes` resolves to "Licenses".

- [ ] **Step 2: Record the new golden**

Run: `./gradlew :feature:account:recordRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true`

The new file appears at `feature/account/screenshot/account_screen_tablet_license.png`.

- [ ] **Step 3: Visually inspect the new golden**

Open `feature/account/screenshot/account_screen_tablet_license.png`. Confirm:
- Two panes side by side.
- Right pane: Licenses screen showing one row ("Compose Runtime" with the Apache-2.0 chip).

- [ ] **Step 4: Run verify**

Run: `./gradlew :feature:account:verifyRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 5: Commit**

```bash
git add feature/account/src/test/java/com/mataku/scrobscrob/account/ui/screen/AccountScreenTest.kt \
        feature/account/screenshot/account_screen_tablet_license.png
git commit -m "$(cat <<'EOF'
test(account): add tablet VRT for Licenses detail pane

Same shape as the Theme tablet VRT but selects the Licenses row. The
provided LicenseViewModel returns a single canned LicenseArtifact so
the right pane has visible content.

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

## Task 6: Cross-cutting verification

**Files:** none (verification only)

- [ ] **Step 1: Run the full unit-test bucket across affected modules**

Run: `./gradlew :feature:account:testDebugUnitTest -PexcludeScreenshotTest=true`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 2: Run the full screenshot-test bucket**

Run: `./gradlew :feature:account:verifyRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true`
Expected: BUILD SUCCESSFUL with five Account goldens passing: `account_screen.png`, `account_screen_light.png`, `account_screen_tablet.png`, `account_screen_tablet_theme.png`, `account_screen_tablet_license.png`.

- [ ] **Step 3: Run architecture and Lint guardrails**

Run: `./gradlew :architecture-spec:test`
Expected: BUILD SUCCESSFUL.

Run: `./gradlew :feature:account:lintDebug`
Expected: BUILD SUCCESSFUL with no new violations from `PreferSunsetListDetailPaneScaffoldDetector`, `UiStateMustBeStateFlow`, `UiStateMustBeImmutable`, or `PreviewNotPrivate`.

- [ ] **Step 4: Run the tablet large-screen smoke test**

Run: `./gradlew :app:pixelTabletApi35DebugAndroidTest -PincludeLargeScreenE2E=true`
Expected: BUILD SUCCESSFUL. `LargeScreenSmokeTest` already exercises the scaffold path generically; if it passes, the Account tab integrates correctly with the rest of the tablet shell.

This GMD run takes ~5–15 minutes once the system image is cached. Per `.claude/rules/testing.md`, do not skip this — the user has flagged that compile-only verification is not enough.

- [ ] **Step 5: Final sanity — `git status` and `git log`**

Run: `git status`
Expected: clean tree (only the un-tracked PNG/MP4/markdown working artifacts that pre-date this branch).

Run: `git log --oneline e326f975^..HEAD`
Expected: six commits — the spec doc plus five from Tasks 1–5.

No additional commit at this step.

---

## Out of scope (explicit non-tasks)

- Selection persistence across configuration change. The existing
  `rememberSunsetListDetailScaffoldState` does not use `rememberSaveable`;
  matching that keeps Account consistent with `ScrobbleScreen` and
  `TopAlbumsScreen`. Add later if the wrapper changes.
- A `ScrobbleSettingScreen` tablet VRT. The screen is fronted by the
  notification-listener permission launcher; the detail pane only renders
  after a system Activity result. Roborazzi cannot easily drive that path.
- A `PrivacyPolicyScreen` tablet VRT. The detail pane embeds an
  `AndroidView { WebView(...) }` that does not paint deterministically
  under Roborazzi.
- Migrating `UI Catalog` from Intent-launched Activity to in-app destination.
  Out of scope per the spec.
