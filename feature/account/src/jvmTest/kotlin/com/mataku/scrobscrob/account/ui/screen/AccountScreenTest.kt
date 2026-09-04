package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mataku.scrobscrob.account.permission.NotificationListenerPermission
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import com.mataku.scrobscrob.account.update.AppUpdateStatus
import com.mataku.scrobscrob.account.update.InAppUpdateManager
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.Image
import com.mataku.scrobscrob.core.entity.LicenseArtifact
import com.mataku.scrobscrob.core.entity.SpdxLicense
import com.mataku.scrobscrob.core.entity.UserInfo
import com.mataku.scrobscrob.test_helper.integration.ScreenshotDevice
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class AccountScreenTest {
  private val inAppUpdateManager = object : InAppUpdateManager {
    override fun updateStatus(): Flow<AppUpdateStatus> = flowOf(AppUpdateStatus.NONE)
    override suspend fun completeUpdate() = Unit

    @Composable
    override fun rememberStartUpdate(): () -> Unit = {}
  }

  private val notificationListenerPermission = object : NotificationListenerPermission {
    override fun isGranted(): Boolean = true

    @Composable
    override fun rememberRequest(onResult: (granted: Boolean) -> Unit): () -> Unit = {}
  }

  private fun stubAccountViewModel(
    theme: AppTheme = AppTheme.DARK
  ): AccountViewModel = mockk<AccountViewModel>(relaxed = true).apply {
    every { uiState } returns MutableStateFlow(
      AccountViewModel.AccountUiState(
        theme = theme,
        events = persistentListOf(),
        appVersion = "1.0.0",
        appUpdateStatus = AppUpdateStatus.NONE,
        imageCacheMB = "100.1",
        userInfo = UserInfo(
          name = "nicole",
          playCount = "102030",
          artistCount = "800",
          trackCount = "1000",
          albumCount = "200",
          imageList = persistentListOf(
            Image(size = "small", url = "https://www.example.com/image.jpg")
          ),
          url = "https://www.example.com"
        )
      )
    )
  }

  @Composable
  private fun Screen(
    viewModel: AccountViewModel,
    themeSelectorViewModel: ThemeSelectorViewModel = mockk(),
    licenseViewModel: LicenseViewModel = mockk(),
  ) {
    AccountScreen(
      viewModel = viewModel,
      inAppUpdateManager = inAppUpdateManager,
      notificationListenerPermission = notificationListenerPermission,
      themeSelectorViewModelProvider = { themeSelectorViewModel },
      licenseViewModelProvider = { licenseViewModel },
      scrobbleSettingViewModelProvider = { mockk() },
      navigateToLogin = {},
      navigateToPrivacyPolicy = {},
      navigateToScrobbleSetting = {},
      navigateToLicenseList = {},
      navigateToThemeSelector = {}
    )
  }

  @Test
  fun layout() {
    val viewModel = stubAccountViewModel()
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Screen(viewModel = viewModel) },
      fileName = "account_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = stubAccountViewModel(theme = AppTheme.LIGHT)
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Screen(viewModel = viewModel) },
      fileName = "account_screen_light.png"
    )
  }

  @Test
  fun layout_tablet() {
    val viewModel = stubAccountViewModel()
    captureScreenshot(
      device = ScreenshotDevice.PixelTablet,
      appTheme = AppTheme.DARK,
      content = { Screen(viewModel = viewModel) },
      fileName = "account_screen_tablet.png"
    )
  }

  @Test
  fun layout_tablet_theme_selected() {
    val viewModel = stubAccountViewModel()
    val themeSelectorViewModel = mockk<ThemeSelectorViewModel> {
      every { uiState } returns MutableStateFlow(
        ThemeSelectorViewModel.ThemeSelectorUiState(
          theme = AppTheme.DARK,
          event = null,
        )
      )
    }
    captureScreenshot(
      device = ScreenshotDevice.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        Screen(
          viewModel = viewModel,
          themeSelectorViewModel = themeSelectorViewModel,
        )
      },
      actionsBeforeCapturing = {
        onNodeWithText("Theme").performClick()
        waitForIdle()
      },
      fileName = "account_screen_tablet_theme.png"
    )
  }

  @Test
  fun layout_tablet_license_selected() {
    val viewModel = stubAccountViewModel()
    val licenseViewModel = mockk<LicenseViewModel> {
      every { uiState } returns MutableStateFlow(
        LicenseViewModel.LicenseUiState(
          licenseList = persistentListOf(
            LicenseArtifact(
              artifactId = "compose-runtime",
              groupId = "androidx.compose.runtime",
              name = "Compose Runtime",
              scm = null,
              spdxLicenses = persistentListOf(
                SpdxLicense(
                  identifier = "Apache-2.0",
                  name = "Apache License 2.0",
                  url = "https://www.apache.org/licenses/LICENSE-2.0",
                )
              ),
              version = "1.7.0",
            ),
          )
        )
      )
    }
    captureScreenshot(
      device = ScreenshotDevice.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        Screen(
          viewModel = viewModel,
          licenseViewModel = licenseViewModel,
        )
      },
      actionsBeforeCapturing = {
        onNodeWithText("Licenses").performClick()
        waitForIdle()
      },
      fileName = "account_screen_tablet_license.png"
    )
  }
}
