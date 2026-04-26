package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTab
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTabRow
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class SunsetTabRowTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_tab_row.png",
    )
  }

  @Test
  fun layout_light() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_tab_row_light.png",
    )
  }
}

@Composable
private fun Content() {
  SunsetTabRow(selectedTabIndex = 0) {
    SunsetTab(selected = true, onClick = {}) {
      SunsetText.Label(
        text = "Tab A",
        modifier = Modifier.padding(16.dp),
      )
    }
    SunsetTab(selected = false, onClick = {}) {
      SunsetText.Label(
        text = "Tab B",
        modifier = Modifier.padding(16.dp),
      )
    }
  }
}
