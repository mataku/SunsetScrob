package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class ThemeSelectorScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private val viewModel: ThemeSelectorViewModel = mockk()

  @Before
  fun setUp() {
    coEvery {
      viewModel.uiState
    }.returns(
      MutableStateFlow(
        ThemeSelectorViewModel.ThemeSelectorUiState(
          theme = AppTheme.FOLLOW_SYSTEM,
          event = null,
        )
      )
    )
  }

  @Test
  fun layout() {
    composeTestRule.captureScreenshot(
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
    composeTestRule.captureScreenshot(
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
    composeTestRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.PixelTablet,
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
