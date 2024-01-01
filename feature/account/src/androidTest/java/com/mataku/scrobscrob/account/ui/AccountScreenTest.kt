package com.mataku.scrobscrob.account.ui

import android.app.Application
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.mataku.scrobscrob.account.AppInfoProvider
import com.mataku.scrobscrob.account.ui.screen.AccountScreen
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.data.repository.ThemeRepository
import com.mataku.scrobscrob.test_helper.integration.assertDisplayableAndClickable
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

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
  fun layout() = runTest {
    val viewModel = AccountViewModel(
      themeRepository,
      sessionRepository,
      appInfoProvider,
      appUpdateManager,
      application
    )
    composeRule.setContent {
      SunsetTheme(theme = AppTheme.DARK) {
        Surface {
          AccountScreen(
            viewModel = viewModel,
            navController = navController
          ) {}
        }
      }
    }
    composeRule.awaitIdle()
    composeRule.apply {
      onNodeWithText("Account").assertIsDisplayed().assertHasNoClickAction()
      onNodeWithText("Scrobble").assertDisplayableAndClickable()
      onNodeWithText("Scrobble from selected apps").assertDisplayableAndClickable()
      onNodeWithText("Theme").assertIsDisplayed().assertHasClickAction()
      onNodeWithText(AppTheme.DARK.displayName).assertDisplayableAndClickable()
      onNodeWithText("Clear cache").assertDisplayableAndClickable()
      onNodeWithText("Licenses").assertDisplayableAndClickable()
      onNodeWithText("Privacy policy").assertDisplayableAndClickable()
      onNodeWithText("v${appVersion}").assertDisplayableAndClickable()
    }
  }
}
