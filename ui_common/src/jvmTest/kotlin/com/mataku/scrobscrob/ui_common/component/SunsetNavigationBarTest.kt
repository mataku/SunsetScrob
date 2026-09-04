package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.ScreenshotDevice
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetNavigationBar
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTab
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetNavigationBarTest {
  @Test
  fun layout() = runTest {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {},
          bottomBar = {
            SunsetNavigationBar(
              selectedTab = SunsetTab.HOME,
              onTabSelected = {},
              modifier = Modifier
                .navigationBarsPadding()
            )
          },
          content = {
            Content(
              modifier = Modifier
            )
          }
        )
      },
      fileName = "sunset_navigation_bar_dark.png"
    )
  }

  @Test
  fun layout_landscape() = runTest {
    captureScreenshot(
      device = ScreenshotDevice.Pixel7Landscape,
      appTheme = AppTheme.DARK,
      content = {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {},
          bottomBar = {
            SunsetNavigationBar(
              selectedTab = SunsetTab.HOME,
              onTabSelected = {},
              modifier = Modifier
                .navigationBarsPadding()
            )
          },
          content = {
            Content(
              modifier = Modifier
            )
          }
        )
      },
      fileName = "sunset_navigation_bar_dark_landscape.png"
    )
  }

  @Test
  fun layout_light() = runTest {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {},
          bottomBar = {
            SunsetNavigationBar(
              selectedTab = SunsetTab.HOME,
              onTabSelected = {},
              modifier = Modifier
                .navigationBarsPadding()
            )
          },
          content = {
            Content(
              modifier = Modifier.padding(it)
            )
          }
        )
      },
      fileName = "sunset_navigation_bar_light.png"
    )
  }

  @Test
  fun layout_tablet() = runTest {
    captureScreenshot(
      device = ScreenshotDevice.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {},
          bottomBar = {
            SunsetNavigationBar(
              selectedTab = SunsetTab.HOME,
              onTabSelected = {},
              modifier = Modifier
                .navigationBarsPadding()
            )
          },
          content = {
            Content(
              modifier = Modifier
            )
          }
        )
      },
      fileName = "sunset_navigation_bar_tablet.png"
    )
  }

}

@Composable
private fun Content(modifier: Modifier = Modifier) {
  LazyColumn(
    modifier = modifier.fillMaxSize()
  ) {
    items(20) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        SunsetText.Body(
          text = "$it Item Item Item Item Item Item Item Item Item Item Item Item Item Item Item Item",
          maxLines = 1
        )
      }
    }
  }
}
