package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetFloatingActionButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIcon
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetFloatingActionButtonTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_floating_action_button.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_floating_action_button_light.png",
    )
  }
}

@Composable
private fun Content() {
  Box(modifier = Modifier.padding(16.dp)) {
    SunsetFloatingActionButton(onClick = {}) {
      SunsetIcon(
        imageVector = Icons.Default.Add,
        contentDescription = "add",
      )
    }
  }
}
