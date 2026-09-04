package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.account.AccountMenu
import com.mataku.scrobscrob.account.generated.resources.Res
import com.mataku.scrobscrob.account.generated.resources.button_back
import com.mataku.scrobscrob.account.generated.resources.button_go_to_setting
import com.mataku.scrobscrob.account.generated.resources.dialog_notification_permission_required
import com.mataku.scrobscrob.account.generated.resources.dialog_notification_permission_required_description
import com.mataku.scrobscrob.account.generated.resources.label_start_update
import com.mataku.scrobscrob.account.generated.resources.label_update_available
import com.mataku.scrobscrob.account.permission.NotificationListenerPermission
import com.mataku.scrobscrob.account.ui.molecule.Profile
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import com.mataku.scrobscrob.account.update.AppUpdateStatus
import com.mataku.scrobscrob.account.update.InAppUpdateManager
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.UserInfo
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetAlertDialog
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetHorizontalDivider
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetListDetailScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar
import com.mataku.scrobscrob.ui_common.component.designsystem.rememberSunsetListDetailScaffoldState
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.isCompactWidth
import com.mataku.scrobscrob.ui_common.style.onSecondaryColor
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountScreen(
  viewModel: AccountViewModel,
  inAppUpdateManager: InAppUpdateManager,
  notificationListenerPermission: NotificationListenerPermission,
  themeSelectorViewModelProvider: @Composable () -> ThemeSelectorViewModel,
  licenseViewModelProvider: @Composable () -> LicenseViewModel,
  scrobbleSettingViewModelProvider: @Composable () -> ScrobbleSettingViewModel,
  navigateToScrobbleSetting: () -> Unit,
  navigateToPrivacyPolicy: () -> Unit,
  navigateToThemeSelector: () -> Unit,
  navigateToLicenseList: () -> Unit,
  navigateToLogin: () -> Unit,
  modifier: Modifier = Modifier
) {
  val openDialog = remember {
    mutableStateOf(false)
  }
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val compact = isCompactWidth()
  val scaffoldState = rememberSunsetListDetailScaffoldState<AccountDetail>()

  val requestNotificationPermission = notificationListenerPermission.rememberRequest { granted ->
    if (granted) {
      if (compact) {
        navigateToScrobbleSetting.invoke()
      } else {
        scaffoldState.selectDetail(AccountDetail.ScrobbleSetting)
      }
    }
  }
  val startUpdate = inAppUpdateManager.rememberStartUpdate()

  val snackbarHostState = LocalSnackbarHostState.current
  val startUpdateMessage = stringResource(Res.string.label_start_update)

  uiState.theme?.let { theme ->
    val onTapLogout: () -> Unit = { openDialog.value = true }
    val onRequestAppUpdate: () -> Unit = {
      if (uiState.appUpdateStatus == AppUpdateStatus.DOWNLOADED) {
        viewModel.completeUpdate()
      } else {
        startUpdate.invoke()
      }
    }

    if (compact) {
      AccountListPane(
        theme = theme,
        appVersion = uiState.appVersion,
        appUpdateStatus = uiState.appUpdateStatus,
        imageCacheMB = uiState.imageCacheMB,
        userInfo = uiState.userInfo,
        isNotificationListenerGranted = notificationListenerPermission::isGranted,
        onTapTheme = navigateToThemeSelector,
        onTapLicense = navigateToLicenseList,
        onTapPrivacyPolicy = navigateToPrivacyPolicy,
        onTapScrobbleSetting = navigateToScrobbleSetting,
        onTapLogout = onTapLogout,
        onTapNotificationSetting = requestNotificationPermission,
        onRequestAppUpdate = onRequestAppUpdate,
        onClearCache = viewModel::clearCache,
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
            appUpdateStatus = uiState.appUpdateStatus,
            imageCacheMB = uiState.imageCacheMB,
            userInfo = uiState.userInfo,
            isNotificationListenerGranted = notificationListenerPermission::isGranted,
            onTapTheme = { scaffoldState.selectDetail(AccountDetail.Theme) },
            onTapLicense = { scaffoldState.selectDetail(AccountDetail.License) },
            onTapPrivacyPolicy = { scaffoldState.selectDetail(AccountDetail.PrivacyPolicy) },
            onTapScrobbleSetting = { scaffoldState.selectDetail(AccountDetail.ScrobbleSetting) },
            onTapLogout = onTapLogout,
            onTapNotificationSetting = requestNotificationPermission,
            onRequestAppUpdate = onRequestAppUpdate,
            onClearCache = viewModel::clearCache,
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

  LaunchedEffect(uiState.events) {
    uiState.events.firstOrNull()?.let {
      when (it) {
        is AccountViewModel.Event.Logout -> {
          openDialog.value = false
          navigateToLogin.invoke()
        }
        is AccountViewModel.Event.UpdateDownloaded -> {
          snackbarHostState.showSnackbar(startUpdateMessage)
          delay(2000L)
          viewModel.completeUpdate()
        }
      }
      viewModel.popEvent(it)
    }
  }

  if (openDialog.value) {
    SunsetAlertDialog(
      title = "Logout?",
      onConfirmButton = {
        viewModel.logout()
      },
      confirmButtonText = "Let me out!",
      dismissButtonText = "Cancel",
      onDismissRequest = {
        openDialog.value = false
      },
      onDismissButton = {
        openDialog.value = false
      }
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AccountContent(
  theme: AppTheme,
  appVersion: String,
  appUpdateStatus: AppUpdateStatus,
  imageCacheMB: String?,
  userInfo: UserInfo?,
  isNotificationListenerGranted: () -> Boolean,
  navigateToScrobbleSetting: () -> Unit,
  navigateToThemeSelector: () -> Unit,
  navigateToLogoutConfirmation: () -> Unit,
  clearCache: () -> Unit,
  navigateToLicenseList: () -> Unit,
  navigateToPrivacyPolicy: () -> Unit,
  navigateToNotificationSetting: () -> Unit,
  requestAppUpdate: () -> Unit,
  modifier: Modifier = Modifier
) {
  val openDialog = remember {
    mutableStateOf(false)
  }
  val openClearCacheConfirmationDialog = remember {
    mutableStateOf(false)
  }
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
  ) {
    if (userInfo != null) {
      item(key = "profile") {
        Profile(
          userInfo = userInfo,
          modifier = Modifier
            .padding(
              horizontal = 16.dp
            )
        )
        SunsetHorizontalDivider(
          modifier = Modifier.padding(vertical = 8.dp),
        )
      }

      item(key = "scrobble") {
        val scrobbleMenu = AccountMenu.SCROBBLE
        AccountMenuCell(
          title = stringResource(scrobbleMenu.titleRes),
          description = scrobbleMenu.descriptionText()
        ) {
          if (isNotificationListenerGranted()) {
            navigateToScrobbleSetting.invoke()
          } else {
            openDialog.value = true
          }
        }
      }

      item(key = "theme") {
        val menu = AccountMenu.THEME
        AccountMenuCell(
          title = stringResource(menu.titleRes),
          description = theme.displayName
        ) {
          navigateToThemeSelector.invoke()
        }
      }
      item(key = "clear_cache") {
        val clearCacheMenu = AccountMenu.CLEAR_CACHE
        AccountMenuCell(
          title = stringResource(clearCacheMenu.titleRes),
          description = if (imageCacheMB != null) {
            "$imageCacheMB MB"
          } else {
            ""
          }
        ) {
          openClearCacheConfirmationDialog.value = true
        }
      }
      item(key = "app_version") {
        val appUpdateMenu = AccountMenu.APP_VERSION
        val updateAvailable = appUpdateStatus != AppUpdateStatus.NONE
        AccountMenuCell(
          title = stringResource(appUpdateMenu.titleRes, appVersion),
          description = if (updateAvailable) {
            stringResource(Res.string.label_update_available)
          } else {
            "Thank you for using the latest version!"
          },
          enabled = updateAvailable
        ) {
          requestAppUpdate.invoke()
        }
      }
      item(key = "logout") {
        val logoutMenu = AccountMenu.LOGOUT
        AccountMenuCell(
          title = stringResource(logoutMenu.titleRes),
          description = logoutMenu.descriptionText()
        ) {
          navigateToLogoutConfirmation.invoke()
        }

        SunsetHorizontalDivider(
          modifier = Modifier.padding(vertical = 8.dp)
        )
      }
      item(key = "licenses") {
        val licenseMenu = AccountMenu.LICENSE
        AccountMenuCell(
          title = stringResource(licenseMenu.titleRes),
          description = ""
        ) {
          navigateToLicenseList.invoke()
        }
      }
      item(key = "privacy_policy") {
        val privacyPolicyMenu = AccountMenu.PRIVACY_POLICY
        AccountMenuCell(
          title = stringResource(privacyPolicyMenu.titleRes),
          description = ""
        ) {
          navigateToPrivacyPolicy.invoke()
        }
      }
    }
  }

  if (openDialog.value) {
    SunsetAlertDialog(
      title = stringResource(Res.string.dialog_notification_permission_required),
      description = stringResource(Res.string.dialog_notification_permission_required_description),
      confirmButtonText = stringResource(Res.string.button_go_to_setting),
      onConfirmButton = {
        openDialog.value = false
        navigateToNotificationSetting.invoke()
      },
      dismissButtonText = stringResource(Res.string.button_back),
      onDismissButton = {
        openDialog.value = false
      },
      onDismissRequest = {
        openDialog.value = false
      },
    )
  }

  if (openClearCacheConfirmationDialog.value) {
    SunsetAlertDialog(
      title = "Clear cache?",
      onConfirmButton = {
        clearCache.invoke()
        openClearCacheConfirmationDialog.value = false
      },
      confirmButtonText = "Clear",
      dismissButtonText = "Cancel",
      onDismissRequest = {
        openClearCacheConfirmationDialog.value = false
      },
      onDismissButton = {
        openClearCacheConfirmationDialog.value = false
      }
    )
  }
}

@Composable
private fun AccountMenu.descriptionText(): String =
  descriptionRes?.let { stringResource(it) } ?: ""

@Composable
private fun AccountMenuCell(
  title: String,
  description: String,
  enabled: Boolean = true,
  onTapAccount: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp)
      .clickable(enabled = enabled) {
        onTapAccount.invoke()
      }
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.SpaceEvenly
  ) {
    SunsetText.Subtitle(text = title)
    if (description.isNotBlank()) {
      SunsetText.Caption(
        text = description,
        color = LocalAppTheme.current.onSecondaryColor()
      )
    }
  }
}

@Composable
private fun AccountListPane(
  theme: AppTheme,
  appVersion: String,
  appUpdateStatus: AppUpdateStatus,
  imageCacheMB: String?,
  userInfo: UserInfo?,
  isNotificationListenerGranted: () -> Boolean,
  onTapTheme: () -> Unit,
  onTapLicense: () -> Unit,
  onTapPrivacyPolicy: () -> Unit,
  onTapScrobbleSetting: () -> Unit,
  onTapLogout: () -> Unit,
  onTapNotificationSetting: () -> Unit,
  onRequestAppUpdate: () -> Unit,
  onClearCache: () -> Unit,
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
      isNotificationListenerGranted = isNotificationListenerGranted,
      navigateToThemeSelector = onTapTheme,
      navigateToLogoutConfirmation = onTapLogout,
      navigateToLicenseList = onTapLicense,
      navigateToPrivacyPolicy = onTapPrivacyPolicy,
      navigateToScrobbleSetting = onTapScrobbleSetting,
      navigateToNotificationSetting = onTapNotificationSetting,
      appUpdateStatus = appUpdateStatus,
      requestAppUpdate = onRequestAppUpdate,
      appVersion = appVersion,
      clearCache = onClearCache,
      imageCacheMB = imageCacheMB,
      userInfo = userInfo,
      modifier = Modifier.padding(paddingValues)
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun AccountContentPreview() {
  SunsetThemePreview {
    AccountContent(
      theme = AppTheme.DARK,
      isNotificationListenerGranted = { true },
      navigateToThemeSelector = {},
      navigateToLogoutConfirmation = {},
      navigateToLicenseList = {},
      navigateToPrivacyPolicy = {},
      navigateToScrobbleSetting = {},
      navigateToNotificationSetting = {},
      requestAppUpdate = {},
      appUpdateStatus = AppUpdateStatus.NONE,
      appVersion = "1.0.0",
      clearCache = {},
      imageCacheMB = "0.1",
      userInfo = UserInfo(
        name = "mataku",
        playCount = "10000",
        albumCount = "100",
        trackCount = "1000",
        artistCount = "100",
        url = "",
        imageList = persistentListOf()
      )
    )
  }
}

internal sealed interface AccountDetail {
  data object Theme : AccountDetail
  data object License : AccountDetail
  data object PrivacyPolicy : AccountDetail
  data object ScrobbleSetting : AccountDetail
}
