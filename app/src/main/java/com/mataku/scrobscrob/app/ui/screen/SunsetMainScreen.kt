package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import com.mataku.scrobscrob.account.ui.navigation.AccountKey
import com.mataku.scrobscrob.account.ui.navigation.accountGraph
import com.mataku.scrobscrob.album.ui.navigation.albumGraph
import com.mataku.scrobscrob.artist.ui.navigation.artistGraph
import com.mataku.scrobscrob.auth.ui.navigation.authGraph
import com.mataku.scrobscrob.chart.ui.navigation.DiscoverKey
import com.mataku.scrobscrob.discover.ui.navigation.discoverGraph
import com.mataku.scrobscrob.home.ui.navigation.HomeKey
import com.mataku.scrobscrob.home.ui.navigation.homeGraph
import com.mataku.scrobscrob.scrobble.ui.navigation.scrobbleGraph
import com.mataku.scrobscrob.ui_common.commonGraph
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetNavigationBar
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetSnackbarHost
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTab
import com.mataku.scrobscrob.ui_common.navigation.LoginKey
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavHost
import com.mataku.scrobscrob.ui_common.navigation.SunsetTabHost
import com.mataku.scrobscrob.ui_common.navigation.rememberSunsetNavBackStack
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun SunsetMainScreen(
  isAuthenticated: Boolean,
  modifier: Modifier = Modifier,
) {
  if (isAuthenticated) {
    var selectedTab by remember { mutableStateOf(SunsetTab.HOME) }

    SunsetScaffold(
      modifier = modifier.fillMaxSize(),
      snackbarHost = { SunsetSnackbarHost(hostState = LocalSnackbarHostState.current) },
      bottomBar = {
        SunsetNavigationBar(
          selectedTab = selectedTab,
          onTabSelected = { selectedTab = it },
          modifier = Modifier.navigationBarsPadding(),
          isVisible = true,
        )
      },
    ) { padding ->
      SunsetTabHost(
        tabs = SunsetTab.entries.toImmutableList(),
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        initialKeyForTab = { tab ->
          when (tab) {
            SunsetTab.HOME -> HomeKey
            SunsetTab.DISCOVER -> DiscoverKey
            SunsetTab.ACCOUNT -> AccountKey
          }
        },
        modifier = Modifier.padding(
          start = padding.calculateStartPadding(LayoutDirection.Ltr),
          end = padding.calculateEndPadding(LayoutDirection.Ltr),
        ),
        bottomBar = { _, _ -> },
      ) {
        homeGraph()
        albumGraph()
        artistGraph()
        scrobbleGraph()
        accountGraph()
        discoverGraph()
        commonGraph()
      }
    }
  } else {
    val backStack = rememberSunsetNavBackStack(initial = LoginKey)
    SunsetNavHost(backStack = backStack, modifier = modifier.fillMaxSize()) {
      authGraph()
      commonGraph()
      accountGraph()
    }
  }
}
