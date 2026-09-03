package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetSwitch
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetSwitchTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_switch.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_switch_light.png",
    )
  }
}

@Composable
private fun Content() {
  Row(
    modifier = Modifier.padding(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    SunsetSwitch(checked = true, onCheckedChange = {})
    SunsetSwitch(checked = false, onCheckedChange = {})
    SunsetSwitch(checked = true, onCheckedChange = {}, enabled = false)
    SunsetSwitch(checked = false, onCheckedChange = {}, enabled = false)
  }
}
