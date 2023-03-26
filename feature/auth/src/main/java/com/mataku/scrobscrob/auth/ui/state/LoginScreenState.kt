package com.mataku.scrobscrob.auth.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.ui_common.navigateToPrivacyPolicy
import com.mataku.scrobscrob.ui_common.navigateToScrobble

class LoginScreenState(
  private val navController: NavController,
  private val viewModel: LoginViewModel
) {
  val uiState: LoginViewModel.UiState
    @Composable get() = viewModel.uiState.collectAsState().value

  fun login(username: String, password: String) {
    viewModel.authorize(
      username = username,
      password = password
    )
  }

  fun navigateToTop() {
    navController.navigateToScrobble(fromAuth = true)
  }

  fun navigateToPrivacyPolicy() {
    navController.navigateToPrivacyPolicy()
  }

  fun popEvent() {
    viewModel.popEvent()
  }

  fun onUsernameUpdate(username: String) {
    viewModel.updateUsername(username)
  }

  fun onPasswordUpdate(password: String) {
    viewModel.updatePassword(password)
  }

  sealed class UiEvent {
    object LoginSuccess : UiEvent()
    object LoginFailed : UiEvent()
    object EmptyUsernameError : UiEvent()
    object EmptyPasswordError : UiEvent()
  }
}

@Composable
fun rememberLoginScreenState(
  navController: NavController,
  viewModel: LoginViewModel = hiltViewModel()
): LoginScreenState =
  remember(navController, viewModel) {
    LoginScreenState(
      navController = navController,
      viewModel = viewModel
    )
  }
