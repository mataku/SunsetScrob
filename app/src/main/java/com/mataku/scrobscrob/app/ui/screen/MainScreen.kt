package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.account.ui.navigation.navigateToAccount
import com.mataku.scrobscrob.app.ui.navigation.NavigationGraph
import com.mataku.scrobscrob.app.ui.navigation.ScreenStyle
import com.mataku.scrobscrob.discover.ui.navigation.navigateToDiscover
import com.mataku.scrobscrob.home.ui.navigation.navigateToHome
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.organism.SunsetNavigationBar
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState
import com.mataku.scrobscrob.ui_common.style.LocalTopAppBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  modifier: Modifier = Modifier
) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val currentScreenStyle = ScreenStyle.fromRoute(route = currentRoute)

  CompositionLocalProvider(
    LocalTopAppBarState provides TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
  ) {
    Scaffold(
      modifier = modifier.fillMaxSize(),
      snackbarHost = {
        SnackbarHost(hostState = LocalSnackbarHostState.current)
      },
      topBar = if (currentScreenStyle == null) {
        {}
      } else {
        {
          TopAppBar(
            title = currentScreenStyle.topAppBarTitle,
            navigationIcon = if (currentScreenStyle.navigationRequired) {
              {
                IconButton(onClick = {
                  navController.popBackStack()
                }) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back"
                  )
                }
              }
            } else {
              {}
            },
            scrollBehavior = if (currentScreenStyle == ScreenStyle.HomeScreenStyle) {
              LocalTopAppBarState.current
            } else {
              null
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
              containerColor = MaterialTheme.colorScheme.background,
              scrolledContainerColor = MaterialTheme.colorScheme.background
            ),
          )
        }
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
          // ignore bottom padding because of custom bottom nav component
          .padding(
            start = it.calculateStartPadding(
              layoutDirection = LayoutDirection.Ltr
            ),
            end = it.calculateEndPadding(
              layoutDirection = LayoutDirection.Ltr
            ),
            top = it.calculateTopPadding()
          )
      )
    }
  }
}
