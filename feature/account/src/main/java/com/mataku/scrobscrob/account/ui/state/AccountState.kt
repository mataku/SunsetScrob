package com.mataku.scrobscrob.account.ui.state

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.navigation.navigateToLicense
import com.mataku.scrobscrob.account.ui.navigation.navigateToScrobbleSetting
import com.mataku.scrobscrob.account.ui.navigation.navigateToThemeSelector
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.ui_common.navigateToLogin
import com.mataku.scrobscrob.ui_common.navigateToPrivacyPolicy

class AccountState(
  private val navController: NavController,
  private val context: Context,
  private val viewModel: AccountViewModel
) {
  val uiState: AccountViewModel.UiState
    @Composable get() = viewModel.uiState.collectAsState().value

  fun navigateToThemeSelector() {
    navController.navigateToThemeSelector()
  }

  fun logout() {
    viewModel.logout()
  }

  fun navigateToLoginScreen() {
    navController.navigateToLogin()
  }

  fun navigateToLicenseScreen() {
    navController.navigateToLicense()
  }

  fun navigateToPrivacyPolicyScreen() {
    navController.navigateToPrivacyPolicy()
  }

  fun navigateToScrobbleSetting() {
    navController.navigateToScrobbleSetting()
  }

  fun showPermissionHelp() {
    Toast.makeText(
      context.applicationContext,
      R.string.label_notification_permission_help,
      Toast.LENGTH_LONG
    ).show()
  }

  fun popEvent() {
    viewModel.popEvent()
  }
}

@Composable
fun rememberAccountState(
  navController: NavController,
  context: Context,
  viewModel: AccountViewModel = hiltViewModel()
): AccountState {
  return remember {
    AccountState(viewModel = viewModel, navController = navController, context = context)
  }
}
