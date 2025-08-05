package com.mataku.scrobscrob.home.ui.molecule

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class HomeTabsTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun layout() {
    composeTestRule.captureScreenshot(
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
    composeTestRule.captureScreenshot(
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
