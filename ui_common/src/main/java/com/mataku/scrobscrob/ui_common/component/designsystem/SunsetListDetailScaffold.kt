package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Stable
class SunsetListDetailScaffoldState<T : Any> internal constructor(
  internal val navigator: ThreePaneScaffoldNavigator<T>,
  private val coroutineScope: CoroutineScope,
  private val hasSelectionState: MutableState<Boolean>,
) {
  val selection: T?
    get() = navigator.currentDestination?.contentKey

  val isDetailVisible: Boolean
    get() = navigator.scaffoldValue[ThreePaneScaffoldRole.Primary] == PaneAdaptedValue.Expanded

  val isListVisible: Boolean
    get() = navigator.scaffoldValue[ThreePaneScaffoldRole.Secondary] == PaneAdaptedValue.Expanded

  val showsBothPanes: Boolean
    get() = isListVisible && isDetailVisible

  fun selectDetail(value: T) {
    hasSelectionState.value = true
    coroutineScope.launch {
      navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, value)
    }
  }

  fun back(): Boolean {
    if (!navigator.canNavigateBack()) return false
    hasSelectionState.value = false
    coroutineScope.launch { navigator.navigateBack() }
    return true
  }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun <T : Any> rememberSunsetListDetailScaffoldState(): SunsetListDetailScaffoldState<T> {
  val hasSelection = remember { mutableStateOf(false) }
  val baseDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
  val effectiveDirective = if (hasSelection.value) {
    baseDirective
  } else {
    baseDirective.copy(maxHorizontalPartitions = 1)
  }
  val navigator = rememberListDetailPaneScaffoldNavigator<T>(
    scaffoldDirective = effectiveDirective,
  )
  val scope = rememberCoroutineScope()
  return remember(navigator, scope) {
    SunsetListDetailScaffoldState(
      navigator = navigator,
      coroutineScope = scope,
      hasSelectionState = hasSelection,
    )
  }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun <T : Any> SunsetListDetailScaffold(
  state: SunsetListDetailScaffoldState<T>,
  listPane: @Composable AnimatedVisibilityScope.() -> Unit,
  detailPane: @Composable AnimatedVisibilityScope.(T?) -> Unit,
  modifier: Modifier = Modifier,
) {
  BackHandler(enabled = state.navigator.canNavigateBack()) {
    state.back()
  }
  ListDetailPaneScaffold(
    directive = state.navigator.scaffoldDirective,
    scaffoldState = state.navigator.scaffoldState,
    listPane = {
      AnimatedPane {
        listPane()
      }
    },
    detailPane = {
      AnimatedPane {
        detailPane(state.selection)
      }
    },
    modifier = modifier,
  )
}
