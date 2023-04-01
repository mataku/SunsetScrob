package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.app.ui.navigation.NavigationGraph
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.organism.SunsetNavigationBar
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
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
