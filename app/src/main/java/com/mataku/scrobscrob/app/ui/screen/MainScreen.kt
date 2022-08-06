package com.mataku.scrobscrob.app.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mataku.scrobscrob.app.ui.molecule.SunsetBottomNavigation
import com.mataku.scrobscrob.app.ui.navigation.NavigationGraph
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.style.LocalScaffoldState

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(username: String?) {
  val navController = rememberAnimatedNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route
  val scaffoldState = rememberScaffoldState()

  CompositionLocalProvider(
    LocalScaffoldState provides scaffoldState
  ) {
    Scaffold(
      topBar = {},
      bottomBar = {
        if (SunsetBottomNavItem.values().map { it.screenRoute }.contains(currentRoute)) {
          SunsetBottomNavigation(navController = navController)
        }
      },
      scaffoldState = scaffoldState
    ) {
      NavigationGraph(
        navController,
        isLoggedIn = username != null
      )
    }
  }
}
