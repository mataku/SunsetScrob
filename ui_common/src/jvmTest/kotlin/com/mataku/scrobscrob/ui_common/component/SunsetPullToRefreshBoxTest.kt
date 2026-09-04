package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetPullToRefreshBox
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetPullToRefreshBoxTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_pull_to_refresh_box.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_pull_to_refresh_box_light.png",
    )
  }
}

@Composable
private fun Content() {
  SunsetPullToRefreshBox(
    isRefreshing = true,
    onRefresh = {},
    modifier = Modifier.fillMaxSize(),
  ) {
    SunsetText.Body(
      text = "Pull to refresh",
      modifier = Modifier.padding(16.dp),
    )
  }
}
