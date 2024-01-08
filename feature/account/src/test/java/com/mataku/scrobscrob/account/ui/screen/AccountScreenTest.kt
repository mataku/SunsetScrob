package com.mataku.scrobscrob.account.ui.screen

import android.app.Application
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.mataku.scrobscrob.account.AppInfoProvider
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.data.repository.ThemeRepository
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
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
class AccountScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private val themeRepository = mockk<ThemeRepository>()
  private val sessionRepository = mockk<SessionRepository>()
  private val appInfoProvider = mockk<AppInfoProvider>()
  private val appUpdateManager = mockk<AppUpdateManager>()
  private val application = mockk<Application>()
  private val appUpdateInfoTask = mockk<Task<AppUpdateInfo>>()
  private val navController = mockk<NavController>()
  private val appVersion = "1.0.0"

  @Before
  fun setup() {
    every {
      appInfoProvider.appVersion()
    }.returns(appVersion)

    coEvery {
      themeRepository.currentTheme()
    }.returns(flowOf(AppTheme.DARK))

    coEvery {
      appUpdateManager.appUpdateInfo
    }.returns(appUpdateInfoTask)

    every {
      appUpdateInfoTask.addOnSuccessListener(any())
    }.returns(appUpdateInfoTask)
  }

  @Test
  fun layout() {
    val viewModel = AccountViewModel(
      themeRepository,
      sessionRepository,
      appInfoProvider,
      appUpdateManager,
      application
    )
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        AccountScreen(
          viewModel = viewModel,
          navController = navController
        ) {}
      },
      fileName = "account_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = AccountViewModel(
      themeRepository,
      sessionRepository,
      appInfoProvider,
      appUpdateManager,
      application
    )
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        AccountScreen(
          viewModel = viewModel,
          navController = navController
        ) {}
      },
      fileName = "account_screen_light.png"
    )
  }

}
