package com.mataku.scrobscrob.account.ui.screen

import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class ScrobbleSettingScreenTest {
  private val viewModel: ScrobbleSettingViewModel = mockk {
    every { uiState } returns MutableStateFlow(
      ScrobbleSettingViewModel.UiState(
        allowedApps = persistentSetOf("com.spotify.music"),
        isLoading = false,
        event = null,
      )
    )
  }

  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        ScrobbleSettingScreen(
          viewModel = viewModel,
          onBackPressed = {},
        )
      },
      fileName = "scrobble_setting_screen.png"
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        ScrobbleSettingScreen(
          viewModel = viewModel,
          onBackPressed = {},
        )
      },
      fileName = "scrobble_setting_screen_light.png"
    )
  }
}
