package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.account.ui.navigation.navigateToAccount
import com.mataku.scrobscrob.album.ui.navigation.navigateToTopAlbums
import com.mataku.scrobscrob.app.ui.navigation.NavigationGraph
import com.mataku.scrobscrob.artist.ui.navigation.navigateToTopArtists
import com.mataku.scrobscrob.chart.ui.navigation.navigateToChart
import com.mataku.scrobscrob.home.ui.navigation.navigateToHome
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
          navigateToChart = navController::navigateToChart,
          navigateToHome = navController::navigateToHome,
          modifier = Modifier
            .navigationBarsPadding()
        )
      }
    },
  ) {
    NavigationGraph(
      navController,
      isLoggedIn = username != null,
      modifier = Modifier
        // ignore bottom padding because of custom bottom nav component
        .padding(
          top = it.calculateTopPadding(),
          start = it.calculateStartPadding(
            layoutDirection = LayoutDirection.Ltr
          ),
          end = it.calculateEndPadding(
            layoutDirection = LayoutDirection.Ltr
          )
        )
    )
  }
}



