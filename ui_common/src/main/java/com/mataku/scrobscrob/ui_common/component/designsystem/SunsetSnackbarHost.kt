package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

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

@Preview
@ShowkaseComposable(name = "SunsetSnackbarHost", group = "Design system")
@Composable
internal fun SunsetSnackbarHostPreview() {
  SunsetThemePreview {
    val hostState = remember { SunsetSnackbarHostState() }
    LaunchedEffect(Unit) {
      hostState.delegate.showSnackbar(
        message = "Hello, Sunset",
        duration = SnackbarDuration.Indefinite,
      )
    }
    Box(modifier = Modifier.fillMaxSize()) {
      SunsetSnackbarHost(
        hostState = hostState,
        modifier = Modifier.align(Alignment.BottomCenter),
      )
    }
  }
}
