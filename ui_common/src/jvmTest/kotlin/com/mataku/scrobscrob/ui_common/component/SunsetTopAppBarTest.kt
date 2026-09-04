package com.mataku.scrobscrob.ui_common.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIcon
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIconButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetTopAppBarTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_top_app_bar.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
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
