package com.mataku.scrobscrob.auth.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mataku.scrobscrob.auth.ui.screen.LoginScreen
import com.mataku.scrobscrob.ui_common.LOGIN_DESTINATION
import com.mataku.scrobscrob.ui_common.navigateToHomeFromAuth
import com.mataku.scrobscrob.ui_common.navigateToPrivacyPolicy

fun NavGraphBuilder.authGraph(navController: NavController) {
  composable(LOGIN_DESTINATION) {
    LoginScreen(
      viewModel = hiltViewModel(),
      navigateToHomeFromAuth = navController::navigateToHomeFromAuth,
      navigateToPrivacyPolicy = navController::navigateToPrivacyPolicy,
      modifier = Modifier
        .padding(
          top = 24.dp
        )
    )
  }
}
