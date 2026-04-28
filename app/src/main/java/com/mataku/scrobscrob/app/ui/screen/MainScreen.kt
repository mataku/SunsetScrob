package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.account.ui.navigation.navigateToAccount
import com.mataku.scrobscrob.app.ui.navigation.NavigationGraph
import com.mataku.scrobscrob.discover.ui.navigation.navigateToDiscover
import com.mataku.scrobscrob.home.ui.navigation.navigateToHome
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetNavigationBar
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetSnackbarHost
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState

@Composable
internal fun MainScreen(
  modifier: Modifier = Modifier
) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  SunsetScaffold(
    modifier = modifier.fillMaxSize(),
    snackbarHost = {
      SunsetSnackbarHost(hostState = LocalSnackbarHostState.current)
    },
    bottomBar = {
      val hasNavigationBarScreen = SunsetBottomNavItem.entries.map { it.screenRoute }
        .contains(currentRoute?.split("?")?.get(0))
      SunsetNavigationBar(
        navController = navController,
        navigateToAccount = navController::navigateToAccount,
        navigateToDiscover = navController::navigateToDiscover,
        navigateToHome = navController::navigateToHome,
        modifier = Modifier
          .navigationBarsPadding(),
        hasNavigationBarScreen = hasNavigationBarScreen
      )
    },
  ) {
    NavigationGraph(
      navController,
      modifier = Modifier
        // ignore top/bottom padding because each screen owns its own AppBar
        // and the bottom nav is a custom overlay
        .padding(
          start = it.calculateStartPadding(
            layoutDirection = LayoutDirection.Ltr
          ),
          end = it.calculateEndPadding(
            layoutDirection = LayoutDirection.Ltr
          ),
        )
    )
  }
}
