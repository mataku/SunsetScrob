package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
class SunsetTextTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_text.png",
    )
  }

  @Test
  fun layout_light() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_text_light.png",
    )
  }
}

@Composable
private fun Content() {
  Column(
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    SunsetText.Headline(text = "Headline")
    SunsetText.Title(text = "Title")
    SunsetText.Subtitle(text = "Subtitle")
    SunsetText.Body(text = "Body")
    SunsetText.Label(text = "Label")
    SunsetText.Caption(text = "Caption")
    SunsetText.ButtonLabel(text = "ButtonLabel")
  }
}
