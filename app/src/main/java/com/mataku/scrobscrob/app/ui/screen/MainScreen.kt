package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.account.ui.navigation.navigateToAccount
import com.mataku.scrobscrob.album.ui.navigation.navigateToTopAlbums
import com.mataku.scrobscrob.app.ui.navigation.NavigationGraph
import com.mataku.scrobscrob.artist.ui.navigation.navigateToTopArtists
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.navigateToScrobble
import com.mataku.scrobscrob.ui_common.organism.SunsetNavigationBar
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState

@Composable
fun MainScreen(username: String?) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  Scaffold(
    snackbarHost = {
      SnackbarHost(hostState = LocalSnackbarHostState.current)
    },
    topBar = {},
    bottomBar = {
      if (SunsetBottomNavItem.values().map { it.screenRoute }.contains(currentRoute)) {
        SunsetNavigationBar(
          navController = navController,
          navigateToTopAlbums = navController::navigateToTopAlbums,
          navigateToTopArtists = navController::navigateToTopArtists,
          navigateToAccount = navController::navigateToAccount,
          navigateToScrobble = navController::navigateToScrobble
        )
      }
    },
  ) {
    NavigationGraph(
      navController,
      isLoggedIn = username != null,
      paddingValues = it
    )
  }
}



