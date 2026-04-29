package com.mataku.scrobscrob.auth.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.metroViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mataku.scrobscrob.auth.ui.screen.LoginScreen
import com.mataku.scrobscrob.ui_common.LOGIN_DESTINATION
import com.mataku.scrobscrob.ui_common.navigateToPrivacyPolicy
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.LoginKey
import com.mataku.scrobscrob.ui_common.navigation.PrivacyPolicyKey
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel

fun NavGraphBuilder.authGraph(navController: NavController) {
  composable(LOGIN_DESTINATION) {
    LoginScreen(
      viewModel = metroViewModel(),
      navigateToHomeFromAuth = navController::navigateToHomeFromAuth,
      navigateToPrivacyPolicy = navController::navigateToPrivacyPolicy,
      modifier = Modifier
        .padding(
          top = 24.dp
        )
    )
  }
}

fun NavController.navigateToHomeFromAuth() {
  navigate("home_route") {
    popUpTo(0)
    launchSingleTop = true
  }
}

fun SunsetNavBuilder.authGraph() {
  destination<LoginKey> { key ->
    LoginScreen(
      viewModel = viewModelFor<LoginViewModel>(key),
      navigateToHomeFromAuth = { /* 認証完了は :app の SunsetMainScreen が isAuthenticated 切替で処理 */ },
      navigateToPrivacyPolicy = { navigate(PrivacyPolicyKey) },
      modifier = Modifier.padding(top = 24.dp),
    )
  }
}
