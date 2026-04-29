package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.app.ui.navigation.NavigationGraph
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetNavigationBar
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetSnackbarHost
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTab
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState

@Composable
internal fun MainScreen(
  startDestination: String,
  modifier: Modifier = Modifier
) {
  val navController = rememberNavController()

  SunsetScaffold(
    modifier = modifier.fillMaxSize(),
    snackbarHost = {
      SunsetSnackbarHost(hostState = LocalSnackbarHostState.current)
    },
    bottomBar = {
      // TODO("Task 21: replaced by SunsetMainScreen")
      SunsetNavigationBar(
        selectedTab = SunsetTab.HOME,
        onTabSelected = {},
        modifier = Modifier
          .navigationBarsPadding(),
      )
    },
  ) {
    NavigationGraph(
      navController,
      startDestination = startDestination,
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
