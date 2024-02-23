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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
import com.mataku.scrobscrob.account.ui.navigation.navigateToLicense
import com.mataku.scrobscrob.account.ui.navigation.navigateToScrobbleSetting
import com.mataku.scrobscrob.account.ui.navigation.navigateToThemeSelector
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetAlertDialog
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.navigateToLogin
import com.mataku.scrobscrob.ui_common.navigateToPrivacyPolicy
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
  viewModel: AccountViewModel,
  navController: NavController,
  showPermissionHelp: () -> Unit
) {
  val systemUiController = rememberSystemUiController()
  val theme = LocalAppTheme.current
  systemUiController.setSystemBarsColor(
    theme.backgroundColor()
  )
  val openDialog = remember {
    mutableStateOf(false)
  }
  val uiState by viewModel.uiState.collectAsState()

  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val notificationPermissionLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      if (NotificationManagerCompat.getEnabledListenerPackages(context)
          .contains(context.packageName)
      ) {
        navController.navigateToScrobbleSetting()
      }
    }

  val appUpdateManager = AppUpdateManagerFactory.create(context.applicationContext)

  val appUpdateLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {

    }

  val snackbarHostState = LocalSnackbarHostState.current

  val listener = InstallStateUpdatedListener { installState ->
    if (installState.installStatus() == InstallStatus.DOWNLOADED) {
      coroutineScope.launch {
        snackbarHostState.showSnackbar(
          context.getString(R.string.label_start_update)
        )
        delay(2000L)
        appUpdateManager.requestCompleteUpdate()
      }
    }
  }

  appUpdateManager.registerListener(listener)

  uiState.theme?.let {
    AccountContent(
      theme = it,
      navigateToThemeSelector = { navController.navigateToThemeSelector() },
      navigateToLogoutConfirmation = {
        openDialog.value = true
      },
      navigateToLicenseList = {
        navController.navigateToLicense()
      },
      navigateToPrivacyPolicy = {
        navController.navigateToPrivacyPolicy()
      },
      navigateToScrobbleSetting = {
        navController.navigateToScrobbleSetting()
      },
      navigateToNotificationSetting = {
        val intent = Intent()
        intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
        notificationPermissionLauncher.launch(intent)
        showPermissionHelp.invoke()
      },
      appUpdateInfo = uiState.appUpdateInfo,
      requestAppUpdate = { appUpdateInfo ->
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
      },
      appVersion = uiState.appVersion,
      clearCache = viewModel::clearCache,
      navigateToUiCatalog = viewModel::navigateToUiCatalog,
      imageCacheMB = uiState.imageCacheMB
    )
  }

  uiState.event?.let {
    when (it) {
      is AccountViewModel.Event.Logout -> {
        openDialog.value = false
        navController.navigateToLogin()
      }
    }
    viewModel.popEvent()
  }

  if (openDialog.value) {
    SunsetAlertDialog(
      title = "Logout?",
      onConfirmButton = {
        viewModel.logout()
      },
      confirmButtonText = "Let me out!",
      dismissButtonText = "NO",
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
  navigateToScrobbleSetting: () -> Unit,
  navigateToThemeSelector: () -> Unit,
  navigateToLogoutConfirmation: () -> Unit,
  clearCache: () -> Unit,
  navigateToLicenseList: () -> Unit,
  navigateToPrivacyPolicy: () -> Unit,
  navigateToNotificationSetting: () -> Unit,
  requestAppUpdate: (AppUpdateInfo) -> Unit,
  navigateToUiCatalog: () -> Unit
) {
  val context = LocalContext.current
  val openDialog = remember {
    mutableStateOf(false)
  }
  val openClearCacheConfirmationDialog = remember {
    mutableStateOf(false)
  }

  LazyColumn(
    content = {
      stickyHeader {
        ContentHeader(text = stringResource(id = R.string.menu_account))
      }
      item {
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
        val menu = AccountMenu.THEME
        AccountMenuCell(
          title = stringResource(id = menu.titleRes),
          description = theme.displayName
        ) {
          navigateToThemeSelector.invoke()
        }
        val logoutMenu = AccountMenu.LOGOUT
        AccountMenuCell(
          title = stringResource(id = logoutMenu.titleRes),
          description = stringResource(id = logoutMenu.descriptionRes)
        ) {
          navigateToLogoutConfirmation.invoke()
        }
      }
      item {
        HorizontalDivider(
          modifier = Modifier.padding(vertical = 8.dp)
        )
        AccountMenuCell(
          title = stringResource(id = AccountMenu.CLEAR_CACHE.titleRes),
          description = if (imageCacheMB != null) {
            "$imageCacheMB MB"
          } else {
            ""
          }
        ) {
          openClearCacheConfirmationDialog.value = true
        }

        val licenseMenu = AccountMenu.LICENSE
        AccountMenuCell(
          title = stringResource(id = licenseMenu.titleRes),
          description = ""
        ) {
          navigateToLicenseList.invoke()
        }
        val privacyPolicyMenu = AccountMenu.PRIVACY_POLICY
        AccountMenuCell(
          title = stringResource(id = privacyPolicyMenu.titleRes),
          description = ""
        ) {
          navigateToPrivacyPolicy.invoke()
        }
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
        if (BuildConfig.DEBUG) {
          AccountMenuCell(
            title = "UI Catalog",
            description = "",
          ) {
            navigateToUiCatalog.invoke()
          }
        }
      }
    },
    modifier = Modifier
      .fillMaxSize()
  )

  if (openDialog.value) {
    AlertDialog(
      onDismissRequest = {
        openDialog.value = false
      },
      title = {
        Text(
          text = stringResource(id = R.string.dialog_notification_permission_required),
        )
      },
      text = {
        Text(text = stringResource(id = R.string.dialog_notification_permission_required_description))
      },
      dismissButton = {
        TextButton(onClick = {
          openDialog.value = false
        }) {
          Text(text = stringResource(id = R.string.button_back), style = SunsetTextStyle.button)
        }
      },
      confirmButton = {
        TextButton(onClick = {
          openDialog.value = false
          navigateToNotificationSetting.invoke()
        }) {
          Text(
            text = stringResource(id = R.string.button_go_to_setting),
            style = SunsetTextStyle.button
          )
        }
      }
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
  Column(modifier = Modifier
    .fillMaxWidth()
    .height(64.dp)
    .clickable(enabled = enabled) {
      onTapAccount.invoke()
    }
    .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.SpaceEvenly
  ) {
    Text(text = title, style = SunsetTextStyle.subtitle)
    if (description.isNotBlank()) {
      Text(
        text = description,
        style = SunsetTextStyle.caption,
        color = MaterialTheme.colorScheme.onSecondary
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun AccountContentPreview() {
  SunsetThemePreview {
    Surface {
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
        imageCacheMB = "0.1"
      )
    }
  }
}
