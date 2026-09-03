package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetChip
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetChipTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_chip.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_chip_light.png",
    )
  }
}

@Composable
private fun Content() {
  Row(
    modifier = Modifier.padding(16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    SunsetChip(
      onClick = {},
      label = { SunsetText.Label(text = "Enabled") },
    )
    SunsetChip(
      onClick = {},
      enabled = false,
      label = { SunsetText.Label(text = "Disabled") },
    )
  }
}
