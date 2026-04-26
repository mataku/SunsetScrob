package com.mataku.scrobscrob.ui_common.component

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetAlertDialog
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class SunsetAlertDialogTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        SunsetAlertDialog(
          title = "Are you sure?",
          description = "This action cannot be undone.",
          confirmButtonText = "OK",
          onConfirmButton = {},
          dismissButtonText = "Cancel",
        )
      },
      fileName = "sunset_alert_dialog.png",
    )
  }

  @Test
  fun layout_light() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        SunsetAlertDialog(
          title = "Are you sure?",
          description = "This action cannot be undone.",
          confirmButtonText = "OK",
          onConfirmButton = {},
          dismissButtonText = "Cancel",
        )
      },
      fileName = "sunset_alert_dialog_light.png",
    )
  }
}
