package com.mataku.scrobscrob.app.ui.screen

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.account.ui.navigation.navigateToAccount
import com.mataku.scrobscrob.album.ui.navigation.navigateToTopAlbums
import com.mataku.scrobscrob.app.ui.navigation.NavigationGraph
import com.mataku.scrobscrob.artist.ui.navigation.navigateToTopArtists
import com.mataku.scrobscrob.chart.ui.navigation.navigateToChart
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.navigateToScrobble
import com.mataku.scrobscrob.ui_common.organism.SunsetNavigationBar
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState

// Suppress warning because of custom transparent background bottom nav
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(username: String?) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  Scaffold(
    modifier = Modifier,
    snackbarHost = {
      SnackbarHost(hostState = LocalSnackbarHostState.current)
    },
    topBar = {},
    bottomBar = {
      if (SunsetBottomNavItem.entries.map { it.screenRoute }.contains(currentRoute)) {
        SunsetNavigationBar(
          navController = navController,
          navigateToScrobble = navController::navigateToScrobble,
          navigateToTopAlbums = navController::navigateToTopAlbums,
          navigateToTopArtists = navController::navigateToTopArtists,
          navigateToAccount = navController::navigateToAccount,
          navigateToChart = navController::navigateToChart
        )
      }
    },
  ) {
    NavigationGraph(
      navController,
      isLoggedIn = username != null,
    )
  }
}



