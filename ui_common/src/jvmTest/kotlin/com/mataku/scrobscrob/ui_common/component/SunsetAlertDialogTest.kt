package com.mataku.scrobscrob.ui_common.component

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetAlertDialog
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetAlertDialogTest {
  @Test
  fun layout() {
    captureScreenshot(
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
    captureScreenshot(
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
