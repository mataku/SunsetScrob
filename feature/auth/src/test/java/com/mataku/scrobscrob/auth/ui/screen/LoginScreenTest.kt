package com.mataku.scrobscrob.auth.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthLauncher
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthResult
import com.mataku.scrobscrob.auth.webauth.WebAuthCallbackChannel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class LoginScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private val launcher = object : LastFmWebAuthLauncher {
    @Composable
    override fun rememberLaunch(onResult: (LastFmWebAuthResult) -> Unit): (String) -> Unit = {}
  }

  private fun viewModel(): LoginViewModel {
    val repository = mockk<SessionRepository>()
    every { repository.webAuthUrl() } returns flowOf("https://www.last.fm/api/auth/?api_key=key&cb=cb")
    return LoginViewModel(repository, WebAuthCallbackChannel())
  }

  @Test
  fun layout() {
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        LoginScreen(
          viewModel = viewModel(),
          webAuthLauncher = launcher,
          navigateToHomeFromAuth = {},
          navigateToPrivacyPolicy = {}
        )
      },
      fileName = "login_screen.png"
    )
  }

  @Test
  fun layout_light() {
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        LoginScreen(
          viewModel = viewModel(),
          webAuthLauncher = launcher,
          navigateToHomeFromAuth = {},
          navigateToPrivacyPolicy = {}
        )
      },
      fileName = "login_screen_light.png"
    )
  }

  @Test
  fun layout_webAuthOpen() {
    val viewModel = viewModel().apply { onWebAuthOpened() }
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        LoginScreen(
          viewModel = viewModel,
          webAuthLauncher = launcher,
          navigateToHomeFromAuth = {},
          navigateToPrivacyPolicy = {}
        )
      },
      fileName = "login_screen_web_auth_open.png"
    )
  }

  @Test
  fun layout_tablet() {
    composeTestRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        LoginScreen(
          viewModel = viewModel(),
          webAuthLauncher = launcher,
          navigateToHomeFromAuth = {},
          navigateToPrivacyPolicy = {}
        )
      },
      fileName = "login_screen_tablet.png"
    )
  }
}
