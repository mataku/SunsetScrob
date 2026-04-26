package com.mataku.scrobscrob.ui_common.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIcon
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIconButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class SunsetTopAppBarTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_top_app_bar.png",
    )
  }

  @Test
  fun layout_light() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_top_app_bar_light.png",
    )
  }
}

@Composable
private fun Content() {
  SunsetTopAppBar(
    title = { SunsetText.Title(text = "Top App Bar") },
    navigationIcon = {
      SunsetIconButton(onClick = {}) {
        SunsetIcon(
          imageVector = Icons.AutoMirrored.Default.ArrowBack,
          contentDescription = "back",
        )
      }
    },
  )
}
