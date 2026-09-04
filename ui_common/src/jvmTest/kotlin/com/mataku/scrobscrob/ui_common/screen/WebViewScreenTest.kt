package com.mataku.scrobscrob.ui_common.screen

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class WebViewScreenTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        WebViewScreen(
          url = "https://www.last.fm",
          onBackPressed = {},
        )
      },
      fileName = "web_view_screen.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        WebViewScreen(
          url = "https://www.last.fm",
          onBackPressed = {},
        )
      },
      fileName = "web_view_screen_light.png",
    )
  }
}
