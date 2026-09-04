package com.mataku.scrobscrob.home.ui.molecule

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class HomeTabsTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        HomeTabs(
          selectedChartIndex = 0,
          onTabTap = {}
        )
      },
      fileName = "home_tabs.png"
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        HomeTabs(
          selectedChartIndex = 0,
          onTabTap = {}
        )
      },
      fileName = "home_tabs_light.png"
    )
  }
}
