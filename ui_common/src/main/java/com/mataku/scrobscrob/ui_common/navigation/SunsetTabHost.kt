package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <Tab : Enum<Tab>> SunsetTabHost(
  tabs: ImmutableList<Tab>,
  selectedTab: Tab,
  onTabSelected: (Tab) -> Unit,
  initialKeyForTab: (Tab) -> SunsetNavKey,
  modifier: Modifier = Modifier,
  bottomBar: @Composable (selectedTab: Tab, onTabSelected: (Tab) -> Unit) -> Unit,
  builder: SunsetNavBuilder.() -> Unit,
) {
  val backStacksByTab: Map<Tab, SunsetNavBackStack> = tabs.associateWith { tab ->
    rememberSunsetNavBackStack(initial = initialKeyForTab(tab))
  }
  val saveableStateHolder: SaveableStateHolder = rememberSaveableStateHolder()

  Box(modifier = modifier) {
    saveableStateHolder.SaveableStateProvider(key = selectedTab.name) {
      SunsetNavHost(
        backStack = backStacksByTab.getValue(selectedTab),
        builder = builder,
      )
    }
    bottomBar(selectedTab, onTabSelected)
  }
}
