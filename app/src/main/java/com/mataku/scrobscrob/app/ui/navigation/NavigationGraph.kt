package com.mataku.scrobscrob.app.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.mataku.scrobscrob.account.ui.navigation.accountGraph
import com.mataku.scrobscrob.album.ui.navigation.albumGraph
import com.mataku.scrobscrob.artist.ui.navigation.artistGraph
import com.mataku.scrobscrob.auth.ui.navigation.authGraph
import com.mataku.scrobscrob.scrobble.ui.navigation.scrobbleGraph
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.commonGraph

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(
  navController: NavHostController,
  isLoggedIn: Boolean
) {
  AnimatedNavHost(
    navController = navController,
    startDestination = if (isLoggedIn) SunsetBottomNavItem.SCROBBLE.screenRoute else "login"
  ) {
    scrobbleGraph(navController)
    albumGraph(navController)
    artistGraph(navController)

    accountGraph(navController)
    authGraph(navController)
    commonGraph()
  }
}
