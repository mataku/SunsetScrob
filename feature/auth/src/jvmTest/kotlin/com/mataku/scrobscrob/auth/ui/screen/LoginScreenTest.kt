package com.mataku.scrobscrob.auth.ui.screen

import androidx.compose.runtime.Composable
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthLauncher
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthResult
import com.mataku.scrobscrob.auth.webauth.WebAuthCallbackChannel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.test_helper.integration.ScreenshotDevice
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class LoginScreenTest {
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
    captureScreenshot(
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
    captureScreenshot(
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
    captureScreenshot(
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
    captureScreenshot(
      device = ScreenshotDevice.PixelTablet,
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
