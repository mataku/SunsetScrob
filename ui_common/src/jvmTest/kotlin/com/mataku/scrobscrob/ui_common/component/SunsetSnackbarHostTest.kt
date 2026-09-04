package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetSnackbarHost
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetSnackbarHostState
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetSnackbarHostTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_snackbar_host.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_snackbar_host_light.png",
    )
  }
}

@Composable
private fun Content() {
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
