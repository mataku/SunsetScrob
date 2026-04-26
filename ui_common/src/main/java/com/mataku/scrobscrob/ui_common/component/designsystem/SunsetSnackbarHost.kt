package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
class SunsetSnackbarHostState internal constructor(
  internal val delegate: SnackbarHostState,
) {
  suspend fun showSnackbar(message: String) {
    delegate.showSnackbar(message)
  }
}

internal fun SunsetSnackbarHostState(): SunsetSnackbarHostState =
  SunsetSnackbarHostState(SnackbarHostState())

@Composable
fun SunsetSnackbarHost(
  hostState: SunsetSnackbarHostState,
  modifier: Modifier = Modifier,
) {
  SnackbarHost(
    hostState = hostState.delegate,
    modifier = modifier,
  )
}
