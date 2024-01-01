package com.mataku.scrobscrob.app.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mataku.scrobscrob.account.ui.navigation.accountGraph
import com.mataku.scrobscrob.album.ui.navigation.albumGraph
import com.mataku.scrobscrob.artist.ui.navigation.artistGraph
import com.mataku.scrobscrob.auth.ui.navigation.authGraph
import com.mataku.scrobscrob.scrobble.ui.navigation.SCROBBLE_NAVIGATION_ROUTE
import com.mataku.scrobscrob.scrobble.ui.navigation.scrobbleGraph
import com.mataku.scrobscrob.ui_common.commonGraph

@Composable
fun NavigationGraph(
  navController: NavHostController,
  isLoggedIn: Boolean,
) {
  NavHost(
    navController = navController,
    startDestination = if (isLoggedIn) SCROBBLE_NAVIGATION_ROUTE else "login",
    modifier = Modifier,
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
    scrobbleGraph(navController)
    albumGraph(navController)
    artistGraph(navController)

    accountGraph(navController)
    authGraph(navController)
    commonGraph()
  }
}
