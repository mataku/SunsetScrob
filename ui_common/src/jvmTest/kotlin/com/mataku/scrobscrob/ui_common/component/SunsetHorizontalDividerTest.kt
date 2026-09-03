package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetHorizontalDivider
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetHorizontalDividerTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_horizontal_divider.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_horizontal_divider_light.png",
    )
  }
}

@Composable
private fun Content() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
  ) {
    SunsetText.Body(text = "Above")
    SunsetHorizontalDivider()
    SunsetText.Body(text = "Below")
  }
}
