package com.mataku.scrobscrob.app.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mataku.scrobscrob.account.ui.navigation.accountGraph
import com.mataku.scrobscrob.auth.ui.navigation.authGraph
import com.mataku.scrobscrob.discover.ui.navigation.discoverGraph
import com.mataku.scrobscrob.home.ui.navigation.HOME_NAVIGATION_ROUTE
import com.mataku.scrobscrob.home.ui.navigation.homeGraph
import com.mataku.scrobscrob.ui_common.commonGraph

@Composable
fun NavigationGraph(
  navController: NavHostController,
  username: String?,
  modifier: Modifier = Modifier
) {
  val isLoggedIn = username != null
  SharedTransitionLayout {
    NavHost(
      navController = navController,
      startDestination = if (isLoggedIn) HOME_NAVIGATION_ROUTE else "login",
      modifier = modifier,
      enterTransition = {
        fadeIn(tween(250))
      },
      exitTransition = {
        fadeOut(tween(250))
      },
      popEnterTransition = {
        EnterTransition.None
      },
      popExitTransition = {
        ExitTransition.None
      },
      contentAlignment = Alignment.TopStart // Workaround for default TopStart animation issue
    ) {
      homeGraph(
        navController = navController,
        sharedTransitionScope = this@SharedTransitionLayout,
      )
      accountGraph(navController, username ?: "")
      discoverGraph(navController)
      authGraph(navController)
      commonGraph(navController)
    }
  }
}
