package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetCircularProgressIndicator
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class SunsetCircularProgressIndicatorTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_circular_progress_indicator.png",
    )
  }

  @Test
  fun layout_light() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_circular_progress_indicator_light.png",
    )
  }
}

@Composable
private fun Content() {
  Box(modifier = Modifier.padding(16.dp)) {
    SunsetCircularProgressIndicator()
  }
}
