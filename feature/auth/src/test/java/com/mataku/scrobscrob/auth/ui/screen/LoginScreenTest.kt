package com.mataku.scrobscrob.auth.ui.screen

import androidx.compose.material3.Surface
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
  qualifiers = RobolectricDeviceQualifiers.Pixel7,
  sdk = [33]
)
class LoginScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun layout() {
    composeTestRule.setContent {
      SunsetThemePreview {
        Surface {
          LoginScreen(
            viewModel = LoginViewModel(mockk()),
            navController = mockk()
          )
        }
      }
    }
    composeTestRule.onNode(isRoot()).captureRoboImage(
      filePath = "screenshot/login_screen.png",
    )
  }

  @Test
  fun layout_light() {
    composeTestRule.setContent {
      SunsetThemePreview(theme = AppTheme.LIGHT) {
        Surface {
          LoginScreen(
            viewModel = LoginViewModel(mockk()),
            navController = mockk()
          )
        }
      }
    }
    composeTestRule.onNode(isRoot()).captureRoboImage(
      filePath = "screenshot/login_screen_light.png",
    )
  }
}
