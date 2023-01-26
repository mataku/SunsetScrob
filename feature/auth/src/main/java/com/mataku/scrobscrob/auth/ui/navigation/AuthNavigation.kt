package com.mataku.scrobscrob.auth.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mataku.scrobscrob.auth.ui.screen.LoginScreen
import com.mataku.scrobscrob.auth.ui.state.rememberLoginScreenState
import com.mataku.scrobscrob.ui_common.LOGIN_DESTINATION

fun NavGraphBuilder.authGraph(navController: NavController) {
  composable(LOGIN_DESTINATION) {
    LoginScreen(
      stateHolder = rememberLoginScreenState(
        navController = navController
      )
    )
  }
}
