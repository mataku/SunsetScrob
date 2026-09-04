package com.mataku.scrobscrob.account.ui.navigation

import androidx.compose.ui.Modifier
import com.mataku.scrobscrob.account.permission.NotificationListenerPermission
import com.mataku.scrobscrob.account.ui.screen.AccountScreen
import com.mataku.scrobscrob.account.ui.screen.LicenseScreen
import com.mataku.scrobscrob.account.ui.screen.PrivacyPolicyScreen
import com.mataku.scrobscrob.account.ui.screen.ScrobbleSettingScreen
import com.mataku.scrobscrob.account.ui.screen.ThemeSelectorScreen
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import com.mataku.scrobscrob.account.update.InAppUpdateManager
import com.mataku.scrobscrob.ui_common.navigation.PrivacyPolicyKey
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor

fun SunsetNavBuilder.accountGraph(
  inAppUpdateManager: InAppUpdateManager,
  notificationListenerPermission: NotificationListenerPermission,
) {
  destination<AccountKey> { key ->
    AccountScreen(
      viewModel = viewModelFor<AccountViewModel>(key),
      inAppUpdateManager = inAppUpdateManager,
      notificationListenerPermission = notificationListenerPermission,
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
      navigateToLogin = { /* handled by the authentication gate, nothing to do here */ },
      navigateToPrivacyPolicy = { navigate(PrivacyPolicyKey) },
      modifier = Modifier,
    )
  }
  destination<ScrobbleSettingKey> { key ->
    ScrobbleSettingScreen(
      viewModel = viewModelFor<ScrobbleSettingViewModel>(key),
      onBackPressed = ::popBackStack,
      modifier = Modifier,
    )
  }
  destination<ThemeSelectorKey> { key ->
    ThemeSelectorScreen(
      viewModel = viewModelFor<ThemeSelectorViewModel>(key),
      onBackPressed = ::popBackStack,
      modifier = Modifier,
    )
  }
  destination<LicenseKey> { key ->
    LicenseScreen(
      viewModel = viewModelFor<LicenseViewModel>(key),
      onBackPressed = ::popBackStack,
      modifier = Modifier,
    )
  }
  destination<PrivacyPolicyKey> {
    PrivacyPolicyScreen(
      onBackPressed = ::popBackStack,
      modifier = Modifier,
    )
  }
}
