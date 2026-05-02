package com.mataku.scrobscrob.account.ui.screen

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.requestAppUpdateInfo
import com.google.android.play.core.ktx.requestCompleteUpdate
import com.mataku.scrobscrob.account.AccountMenu
import com.mataku.scrobscrob.account.BuildConfig
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.molecule.Profile
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
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
import kotlinx.coroutines.launch

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
) {
  val openDialog = remember {
    mutableStateOf(false)
  }
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val compact = isCompactWidth()
  val scaffoldState = rememberSunsetListDetailScaffoldState<AccountDetail>()

  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
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

  val appUpdateManager = AppUpdateManagerFactory.create(context.applicationContext)

  val appUpdateLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {

    }

  val snackbarHostState = LocalSnackbarHostState.current
  val resources = LocalResources.current

  val listener = InstallStateUpdatedListener { installState ->
    if (installState.installStatus() == InstallStatus.DOWNLOADED) {
      coroutineScope.launch {
        snackbarHostState.showSnackbar(
          resources.getString(R.string.label_start_update)
        )
        delay(2000L)
        appUpdateManager.requestCompleteUpdate()
      }
    }
  }

  appUpdateManager.registerListener(listener)

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

  LaunchedEffect(uiState.events) {
    uiState.events.firstOrNull()?.let {
      when (it) {
        is AccountViewModel.Event.Logout -> {
          openDialog.value = false
          navigateToLogin.invoke()
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
  appUpdateInfo: AppUpdateInfo?,
  imageCacheMB: String?,
  userInfo: UserInfo?,
  navigateToScrobbleSetting: () -> Unit,
  navigateToThemeSelector: () -> Unit,
  navigateToLogoutConfirmation: () -> Unit,
  clearCache: () -> Unit,
  navigateToLicenseList: () -> Unit,
  navigateToPrivacyPolicy: () -> Unit,
  navigateToNotificationSetting: () -> Unit,
  requestAppUpdate: (AppUpdateInfo) -> Unit,
  navigateToUiCatalog: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
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
          title = stringResource(id = scrobbleMenu.titleRes),
          description = stringResource(id = scrobbleMenu.descriptionRes)
        ) {
          if (NotificationManagerCompat.getEnabledListenerPackages(context)
              .contains(context.packageName)
          ) {
            navigateToScrobbleSetting.invoke()
          } else {
            openDialog.value = true
          }
        }
      }

      item(key = "theme") {
        val menu = AccountMenu.THEME
        AccountMenuCell(
          title = stringResource(id = menu.titleRes),
          description = theme.displayName
        ) {
          navigateToThemeSelector.invoke()
        }
      }
      item(key = "clear_cache") {
        val clearCacheMenu = AccountMenu.CLEAR_CACHE
        AccountMenuCell(
          title = stringResource(id = clearCacheMenu.titleRes),
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
        val updateAvailable =
          appUpdateInfo?.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE || appUpdateInfo?.installStatus() == InstallStatus.DOWNLOADED
        AccountMenuCell(
          title = stringResource(id = appUpdateMenu.titleRes, appVersion),
          description = if (updateAvailable) {
            stringResource(id = R.string.label_update_available)
          } else {
            "Thank you for using the latest version!"
          },
          enabled = updateAvailable
        ) {
          requestAppUpdate.invoke(appUpdateInfo!!)
        }
      }
      item(key = "logout") {
        val logoutMenu = AccountMenu.LOGOUT
        AccountMenuCell(
          title = stringResource(id = logoutMenu.titleRes),
          description = stringResource(id = logoutMenu.descriptionRes)
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
          title = stringResource(id = licenseMenu.titleRes),
          description = ""
        ) {
          navigateToLicenseList.invoke()
        }
      }
      item(key = "privacy_policy") {
        val privacyPolicyMenu = AccountMenu.PRIVACY_POLICY
        AccountMenuCell(
          title = stringResource(id = privacyPolicyMenu.titleRes),
          description = ""
        ) {
          navigateToPrivacyPolicy.invoke()
        }
      }

      if (BuildConfig.DEBUG) {
        item(key = "ui_catalog") {
          AccountMenuCell(
            title = "UI Catalog",
            description = "",
          ) {
            navigateToUiCatalog.invoke()
          }
        }
      }
    }
  }

  if (openDialog.value) {
    SunsetAlertDialog(
      title = stringResource(id = R.string.dialog_notification_permission_required),
      description = stringResource(id = R.string.dialog_notification_permission_required_description),
      confirmButtonText = stringResource(id = R.string.button_go_to_setting),
      onConfirmButton = {
        openDialog.value = false
        navigateToNotificationSetting.invoke()
      },
      dismissButtonText = stringResource(id = R.string.button_back),
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

@Preview(showBackground = true)
@Composable
private fun AccountContentPreview() {
  SunsetThemePreview {
    AccountContent(
      theme = AppTheme.DARK,
      navigateToThemeSelector = {},
      navigateToLogoutConfirmation = {},
      navigateToLicenseList = {},
      navigateToPrivacyPolicy = {},
      navigateToScrobbleSetting = {},
      navigateToNotificationSetting = {},
      requestAppUpdate = {},
      appUpdateInfo = null,
      appVersion = "1.0.0",
      clearCache = {},
      navigateToUiCatalog = {},
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
