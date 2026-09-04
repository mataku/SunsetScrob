package com.mataku.scrobscrob.account.ui.viewmodel

import com.mataku.scrobscrob.account.AppInfoProvider
import com.mataku.scrobscrob.account.update.AppUpdateStatus
import com.mataku.scrobscrob.account.update.InAppUpdateManager
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.Image
import com.mataku.scrobscrob.core.entity.UserInfo
import com.mataku.scrobscrob.data.repository.FileRepository
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.data.repository.ThemeRepository
import com.mataku.scrobscrob.data.repository.UserRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

private fun createViewModel(
  username: String? = "matakucom",
  themeFlow: kotlinx.coroutines.flow.Flow<AppTheme> = flowOf(AppTheme.DARK),
  cacheFlow: kotlinx.coroutines.flow.Flow<Double> = flowOf(0.0),
  userInfoFlow: kotlinx.coroutines.flow.Flow<UserInfo> = flowOf(),
  updateStatusFlow: kotlinx.coroutines.flow.Flow<AppUpdateStatus> = flowOf(AppUpdateStatus.NONE),
  sessionRepository: SessionRepository = mockk(),
  inAppUpdateManager: InAppUpdateManager = mockk(),
): AccountViewModel {
  val usernameRepository = mockk<UsernameRepository>()
  every { usernameRepository.asyncUsername() } returns flowOf(username)
  val themeRepository = mockk<ThemeRepository>()
  coEvery { themeRepository.currentTheme() } returns themeFlow
  val appInfoProvider = mockk<AppInfoProvider>()
  every { appInfoProvider.appVersion() } returns "1.0.0"
  every { inAppUpdateManager.updateStatus() } returns updateStatusFlow
  val fileRepository = mockk<FileRepository>()
  every { fileRepository.cacheImageDirMBSize() } returns cacheFlow
  val userRepository = mockk<UserRepository>()
  coEvery { userRepository.getInfo(any()) } returns userInfoFlow

  return AccountViewModel(
    usernameRepository = usernameRepository,
    themeRepository = themeRepository,
    sessionRepository = sessionRepository,
    appInfoProvider = appInfoProvider,
    inAppUpdateManager = inAppUpdateManager,
    fileRepository = fileRepository,
    userRepository = userRepository,
  )
}

class AccountViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("init") {
    it("collects theme, app version, and image cache size into UI state") {
      val viewModel = createViewModel(
        themeFlow = flowOf(AppTheme.OCEAN),
        cacheFlow = flowOf(12.345),
      )

      viewModel.uiState.value.let { state ->
        state.theme shouldBe AppTheme.OCEAN
        state.appVersion shouldBe "1.0.0"
        state.imageCacheMB shouldBe "12.35"
      }
    }

    it("falls back to AppTheme.DARK when the theme flow fails") {
      val viewModel = createViewModel(
        themeFlow = flow { throw RuntimeException("boom") },
      )

      viewModel.uiState.value.theme shouldBe AppTheme.DARK
    }

    it("collects userInfo when username is non-empty") {
      val userInfo = UserInfo(
        name = "matakucom",
        playCount = "100",
        artistCount = "10",
        trackCount = "50",
        albumCount = "20",
        imageList = persistentListOf(Image(size = "extralarge", url = "https://example.com")),
        url = "https://www.last.fm/user/matakucom",
      )
      val viewModel = createViewModel(userInfoFlow = flowOf(userInfo))

      viewModel.uiState.value.userInfo shouldBe userInfo
    }

    it("skips userInfo lookup when username is null") {
      val viewModel = createViewModel(username = null)

      viewModel.uiState.value.userInfo shouldBe null
    }

    it("reflects the in-app update status into UI state") {
      val viewModel = createViewModel(
        updateStatusFlow = flowOf(AppUpdateStatus.NONE, AppUpdateStatus.AVAILABLE),
      )

      viewModel.uiState.value.appUpdateStatus shouldBe AppUpdateStatus.AVAILABLE
    }

    it("keeps AppUpdateStatus.NONE when the update status flow fails") {
      val viewModel = createViewModel(
        updateStatusFlow = flow { throw RuntimeException("boom") },
      )

      viewModel.uiState.value.appUpdateStatus shouldBe AppUpdateStatus.NONE
    }

    it("emits UpdateDownloaded when the status turns DOWNLOADED after the initial status") {
      val viewModel = createViewModel(
        updateStatusFlow = flowOf(AppUpdateStatus.AVAILABLE, AppUpdateStatus.DOWNLOADED),
      )

      viewModel.uiState.value.events shouldBe listOf(AccountViewModel.Event.UpdateDownloaded)
    }

    it("does not emit UpdateDownloaded when the initial status is already DOWNLOADED") {
      val viewModel = createViewModel(
        updateStatusFlow = flowOf(AppUpdateStatus.DOWNLOADED),
      )

      viewModel.uiState.value.appUpdateStatus shouldBe AppUpdateStatus.DOWNLOADED
      viewModel.uiState.value.events.shouldBeEmpty()
    }
  }

  describe("#completeUpdate") {
    it("delegates to the in-app update manager") {
      val inAppUpdateManager = mockk<InAppUpdateManager>()
      coEvery { inAppUpdateManager.completeUpdate() } returns Unit

      val viewModel = createViewModel(inAppUpdateManager = inAppUpdateManager)
      viewModel.completeUpdate()

      coVerify(exactly = 1) { inAppUpdateManager.completeUpdate() }
    }

    it("swallows failures from the in-app update manager") {
      val inAppUpdateManager = mockk<InAppUpdateManager>()
      coEvery { inAppUpdateManager.completeUpdate() } throws RuntimeException("boom")

      val viewModel = createViewModel(inAppUpdateManager = inAppUpdateManager)
      viewModel.completeUpdate()

      viewModel.uiState.value.appUpdateStatus shouldBe AppUpdateStatus.NONE
    }
  }

  describe("#logout") {
    it("emits a Logout event after the session repository completes") {
      val sessionRepository = mockk<SessionRepository>()
      coEvery { sessionRepository.logout() } returns flowOf(Unit)

      val viewModel = createViewModel(sessionRepository = sessionRepository)
      viewModel.logout()

      viewModel.uiState.value.events shouldBe listOf(AccountViewModel.Event.Logout)
    }

    it("still emits a Logout event when the repository fails (errors are swallowed)") {
      val sessionRepository = mockk<SessionRepository>()
      coEvery { sessionRepository.logout() } returns flow { throw RuntimeException("boom") }

      val viewModel = createViewModel(sessionRepository = sessionRepository)
      viewModel.logout()

      viewModel.uiState.value.events shouldBe listOf(AccountViewModel.Event.Logout)
    }
  }

  describe("#popEvent") {
    it("removes the popped event from the events list") {
      val sessionRepository = mockk<SessionRepository>()
      coEvery { sessionRepository.logout() } returns flowOf(Unit)

      val viewModel = createViewModel(sessionRepository = sessionRepository)
      viewModel.logout()
      viewModel.popEvent(AccountViewModel.Event.Logout)

      viewModel.uiState.value.events.shouldBeEmpty()
    }
  }
})
