package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTextField
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetTextFieldTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_text_field.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_text_field_light.png",
    )
  }
}

@Composable
private fun Content() {
  Column(
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    SunsetTextField(
      value = "",
      onValueChange = {},
      label = { SunsetText.Label(text = "Empty") },
    )
    SunsetTextField(
      value = "Hello",
      onValueChange = {},
      label = { SunsetText.Label(text = "Filled") },
    )
    SunsetTextField(
      value = "Disabled",
      onValueChange = {},
      enabled = false,
      label = { SunsetText.Label(text = "Disabled") },
    )
  }
}
