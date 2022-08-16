package com.mataku.scrobscrob.auth.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.mataku.scrobscrob.auth.ui.screen.LoginScreen
import com.mataku.scrobscrob.auth.ui.state.rememberLoginScreenState
import com.mataku.scrobscrob.ui_common.LOGIN_DESTINATION

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authGraph(navController: NavController) {
  composable(LOGIN_DESTINATION) {
    LoginScreen(
      stateHolder = rememberLoginScreenState(
        navController = navController
      )
    )
  }
}
