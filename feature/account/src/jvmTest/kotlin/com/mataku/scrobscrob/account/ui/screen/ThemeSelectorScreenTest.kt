package com.mataku.scrobscrob.account.ui.screen

import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.ScreenshotDevice
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class ThemeSelectorScreenTest {
  private val viewModel: ThemeSelectorViewModel = mockk {
    every { uiState } returns MutableStateFlow(
      ThemeSelectorViewModel.ThemeSelectorUiState(
        theme = AppTheme.FOLLOW_SYSTEM,
        event = null,
      )
    )
  }

  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        ThemeSelectorScreen(
          viewModel = viewModel,
          onBackPressed = {},
        )
      },
      fileName = "theme_selector_screen.png"
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        ThemeSelectorScreen(
          viewModel = viewModel,
          onBackPressed = {},
        )
      },
      fileName = "theme_selector_screen_light.png"
    )
  }

  @Test
  fun layout_tablet() {
    captureScreenshot(
      device = ScreenshotDevice.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        ThemeSelectorScreen(
          viewModel = viewModel,
          onBackPressed = {},
        )
      },
      fileName = "theme_selector_screen_tablet.png"
    )
  }
}
