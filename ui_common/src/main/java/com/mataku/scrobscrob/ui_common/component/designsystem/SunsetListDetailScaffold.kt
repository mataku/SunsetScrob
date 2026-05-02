package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculateThreePaneScaffoldValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Stable
class SunsetListDetailScaffoldState<T : Any> internal constructor(
  private val selectionState: MutableState<T?>,
) {
  val selection: T?
    get() = selectionState.value

  fun selectDetail(value: T) {
    selectionState.value = value
  }

  fun back(): Boolean {
    if (selectionState.value == null) return false
    selectionState.value = null
    return true
  }
}

@Composable
fun <T : Any> rememberSunsetListDetailScaffoldState(): SunsetListDetailScaffoldState<T> {
  val selection = remember { mutableStateOf<T?>(null) }
  return remember(selection) {
    SunsetListDetailScaffoldState(selection)
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
  BackHandler(enabled = state.selection != null) {
    state.back()
  }

  // Compute directive + scaffold value atomically from the same selection state
  // so the layout (sizing) and pane visibility flip together. The navigator-based
  // approach raced because navigator.navigateTo runs in a coroutine relative to the
  // synchronous directive update, briefly producing single-pane Detail mid-tap.
  val baseDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
  val effectiveDirective = if (state.selection == null) {
    baseDirective.copy(maxHorizontalPartitions = 1)
  } else {
    baseDirective
  }
  val currentDestination: ThreePaneScaffoldDestinationItem<Any> = if (state.selection == null) {
    ThreePaneScaffoldDestinationItem(pane = ListDetailPaneScaffoldRole.List, contentKey = null)
  } else {
    ThreePaneScaffoldDestinationItem(
      pane = ListDetailPaneScaffoldRole.Detail,
      contentKey = state.selection,
    )
  }
  val effectiveValue = calculateThreePaneScaffoldValue(
    maxHorizontalPartitions = effectiveDirective.maxHorizontalPartitions,
    adaptStrategies = ListDetailPaneScaffoldDefaults.adaptStrategies(),
    currentDestination = currentDestination,
  )

  ListDetailPaneScaffold(
    directive = effectiveDirective,
    value = effectiveValue,
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
