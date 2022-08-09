package com.mataku.scrobscrob.account.ui.state

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel

class AccountState(
  private val navController: NavController,
  private val context: Context,
  private val viewModel: AccountViewModel
) {
  val uiState: AccountViewModel.UiState
    @Composable get() = viewModel.uiState.collectAsState().value

  fun navigateToThemeSelector() {
    navController.navigate("theme")
  }

  fun logout() {
    viewModel.logout()
  }

  fun navigateToLoginScreen() {
    navController.navigate("login") {
      launchSingleTop = true
      popUpTo(
        navController.graph.findStartDestination().id
      ) {
        inclusive = true
      }
    }
  }

  fun navigateToLicenseScreen() {
    navController.navigate("license")
  }

  fun navigateToPrivacyPolicyScreen() {
    navController.navigate("privacy_policy")
  }

  fun navigateToScrobbleSetting() {
    navController.navigate("scrobble_setting")
  }

  fun navigateToNotificationSetting() {
    val intent = Intent()
    intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
    ContextCompat.startActivity(context, intent, null)
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
