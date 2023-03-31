package com.mataku.scrobscrob.app.ui.screen

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.mataku.scrobscrob.ui_common.organism.SunsetNavigationBar3
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
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
        SunsetNavigationBar3(
          navController = navController,
          navigateToScrobble = navController::navigateToScrobble,
          navigateToAccount = navController::navigateToAccount,
          navigateToTopAlbums = navController::navigateToTopAlbums,
          navigateToTopArtists = navController::navigateToTopArtists
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
