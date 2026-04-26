package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class SunsetBottomSheetTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        Column {
          SunsetText.Body(
            text = "Hello",
          )
          Spacer(modifier = Modifier.height(20.dp))
          SunsetText.Body(
            text = "Helloooooooooooooooooooooooo",
          )
        }
      },
      fileName = "sunset_bottom_sheet.png",
    )
  }

  @Test
  fun layout_light() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        Column {
          SunsetText.Body(
            text = "Hello",
          )
          Spacer(modifier = Modifier.height(20.dp))
          SunsetText.Body(
            text = "Helloooooooooooooooooooooooo",
          )
        }
      },
      fileName = "sunset_bottom_sheet_light.png",
    )
  }

}
