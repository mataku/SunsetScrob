package com.mataku.scrobscrob.account.ui.navigation

import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.screen.ThemeSelectorScreen
import com.mataku.scrobscrob.account.ui.screen.AccountScreen
import com.mataku.scrobscrob.account.ui.screen.LicenseScreen
import com.mataku.scrobscrob.account.ui.screen.PrivacyPolicyScreen
import com.mataku.scrobscrob.account.ui.screen.ScrobbleSettingScreen
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import com.mataku.scrobscrob.ui_common.navigation.PrivacyPolicyKey
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor

fun SunsetNavBuilder.accountGraph() {
  destination<AccountKey> { key ->
    val context = LocalContext.current
    AccountScreen(
      viewModel = viewModelFor<AccountViewModel>(key),
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
