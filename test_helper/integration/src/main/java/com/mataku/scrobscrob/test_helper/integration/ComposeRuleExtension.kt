package com.mataku.scrobscrob.test_helper.integration

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@OptIn(ExperimentalRoborazziApi::class)
fun ComposeContentTestRule.captureScreenshot(
  appTheme: AppTheme,
  fileName: String,
  content: @Composable () -> Unit,
  actionsBeforeCapturing: () -> Unit = {}
) {
  this.setContent {
    SunsetThemePreview(theme = appTheme) {
      Surface {
        content()
      }
    }
  }

  actionsBeforeCapturing.invoke()

  this.onNode(isRoot()).captureRoboImage(
    filePath = "screenshot/${fileName}",
    roborazziOptions = RoborazziOptions(
      compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0.01f)
    )
  )
}
