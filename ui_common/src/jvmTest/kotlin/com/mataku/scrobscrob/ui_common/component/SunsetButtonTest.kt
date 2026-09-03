package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTextButton
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetButtonTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_button.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_button_light.png",
    )
  }
}

@androidx.compose.runtime.Composable
private fun Content() {
  Column(
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    SunsetButton(onClick = {}) {
      SunsetText.ButtonLabel(text = "Enabled")
    }
    SunsetButton(onClick = {}, enabled = false) {
      SunsetText.ButtonLabel(text = "Disabled")
    }
    SunsetTextButton.Label(text = "Text Button", onClick = {})
    SunsetTextButton.Label(text = "Text Disabled", onClick = {}, enabled = false)
  }
}
