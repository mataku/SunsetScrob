package com.mataku.scrobscrob.scrobble.ui.state

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleTopBarViewModel

class ScrobbleTopBarState(
  private val navController: NavController,
  private val viewModel: ScrobbleTopBarViewModel,
  private val context: Context
) {
  val uiState: ScrobbleTopBarViewModel.UiState
    @Composable get() = viewModel.uiState.collectAsState().value

  fun navigateToLoginScreen() {
    navController.navigate("login") {
      launchSingleTop = true
    }
  }

  fun navigateToNotificationListenerSettings() {
    val intent = Intent()
    intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
    context.startActivity(intent)
  }

  fun logout() {
    viewModel.logout()
  }
}

@Composable
fun rememberScrobbleTopBarState(
  navController: NavController,
  viewModel: ScrobbleTopBarViewModel = hiltViewModel(),
  context: Context = LocalContext.current
): ScrobbleTopBarState =
  remember {
    ScrobbleTopBarState(
      navController, viewModel, context
    )
  }
