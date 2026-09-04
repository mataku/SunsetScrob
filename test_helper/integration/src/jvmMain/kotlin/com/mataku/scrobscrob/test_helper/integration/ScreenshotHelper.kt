package com.mataku.scrobscrob.test_helper.integration

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.v2.runSkikoComposeUiTest
import androidx.compose.ui.unit.Density
import com.github.takahirom.roborazzi.RoborazziOptions
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import io.github.takahirom.roborazzi.captureRoboImage
import java.io.File

private val DefaultRoborazziOptions = RoborazziOptions(
  compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0.005F),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTestApi::class)
fun captureScreenshot(
  appTheme: AppTheme,
  fileName: String,
  content: @Composable () -> Unit,
  device: ScreenshotDevice = ScreenshotDevice.Pixel7,
  actionsBeforeCapturing: ComposeUiTest.() -> Unit = {},
) {
  runSkikoComposeUiTest(
    size = Size(device.widthPx, device.heightPx),
    density = Density(device.density),
  ) {
    setContent {
      SunsetThemePreview(theme = appTheme) {
        CompositionLocalProvider(LocalRippleConfiguration provides null) {
          content()
        }
      }
    }
    actionsBeforeCapturing()
    val roots = onAllNodes(isRoot()).fetchSemanticsNodes()
    val largestRoot = roots.indices.maxBy { roots[it].size.width * roots[it].size.height }
    onAllNodes(isRoot())[largestRoot].captureRoboImage(
      filePath = File("screenshot", fileName).path,
      roborazziOptions = DefaultRoborazziOptions,
    )
  }
}
