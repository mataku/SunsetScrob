package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetBottomSheet
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetBottomSheetTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        SunsetBottomSheet(
          sheetContent = {
            SunsetText.Body(
              text = "Sheet content",
              modifier = Modifier.padding(16.dp),
            )
          },
          sheetPeekHeight = 80.dp,
        ) { padding ->
          SunsetText.Body(
            text = "Main content",
            modifier = Modifier
              .fillMaxSize()
              .padding(padding)
              .padding(16.dp),
          )
        }
      },
      fileName = "sunset_bottom_sheet.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        SunsetBottomSheet(
          sheetContent = {
            SunsetText.Body(
              text = "Sheet content",
              modifier = Modifier.padding(16.dp),
            )
          },
          sheetPeekHeight = 80.dp,
        ) { padding ->
          SunsetText.Body(
            text = "Main content",
            modifier = Modifier
              .fillMaxSize()
              .padding(padding)
              .padding(16.dp),
          )
        }
      },
      fileName = "sunset_bottom_sheet_light.png",
    )
  }
}
