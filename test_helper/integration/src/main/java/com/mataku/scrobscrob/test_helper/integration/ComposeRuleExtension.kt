package com.mataku.scrobscrob.test_helper.integration

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import org.robolectric.RuntimeEnvironment

private val DefaultRoborazziOptions = RoborazziOptions(
  compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0.005F),
)

fun ComposeContentTestRule.captureScreenshot(
  appTheme: AppTheme,
  fileName: String,
  content: @Composable () -> Unit,
  device: String = RobolectricDeviceQualifiers.Pixel7,
  actionsBeforeCapturing: () -> Unit = {}
) {
  RuntimeEnvironment.setQualifiers(device)

  this.setContent {
    SunsetThemePreview(theme = appTheme) {
      content()
    }
  }

  actionsBeforeCapturing.invoke()

  this.onNode(isRoot()).captureRoboImage(
    filePath = "screenshot/${fileName}",
    roborazziOptions = DefaultRoborazziOptions,
  )
}


