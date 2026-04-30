package com.mataku.scrobscrob.account.ui.viewmodel

import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class ScrobbleSettingViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("init") {
    it("loads allowed apps from the repository") {
      val repository = mockk<ScrobbleSettingRepository>()
      val allowed = setOf("com.spotify.music")
      coEvery { repository.allowedAppsFlow() } returns flowOf(allowed)

      val viewModel = ScrobbleSettingViewModel(repository)

      viewModel.uiState.value.allowedApps shouldBe allowed
      viewModel.uiState.value.isLoading shouldBe false
    }

    it("swallows errors from the allowed-apps flow without crashing") {
      val repository = mockk<ScrobbleSettingRepository>()
      coEvery { repository.allowedAppsFlow() } returns flow { throw RuntimeException("boom") }

      val viewModel = ScrobbleSettingViewModel(repository)

      viewModel.uiState.value.allowedApps.size shouldBe 0
    }
  }

  describe("#changeAppScrobbleState") {
    context("appName cannot be mapped to a package") {
      it("ignores the request entirely") {
        val repository = mockk<ScrobbleSettingRepository>()
        coEvery { repository.allowedAppsFlow() } returns flowOf(emptySet())

        val viewModel = ScrobbleSettingViewModel(repository)
        viewModel.changeAppScrobbleState(appName = "Unknown App", enable = true)

        coVerify(exactly = 0) { repository.allowApp(any()) }
        coVerify(exactly = 0) { repository.disallowApp(any()) }
      }
    }

    context("enable=true with a known app") {
      it("calls allowApp and emits AllowAppDone") {
        val repository = mockk<ScrobbleSettingRepository>()
        coEvery { repository.allowedAppsFlow() } returns flowOf(emptySet())
        coEvery { repository.allowApp("com.spotify.music") } returns flowOf(Unit)

        val viewModel = ScrobbleSettingViewModel(repository)
        viewModel.changeAppScrobbleState(appName = "Spotify", enable = true)

        viewModel.uiState.value.event shouldBe ScrobbleSettingViewModel.UiEvent.AllowAppDone
      }
    }

    context("enable=false with a known app") {
      it("calls disallowApp and emits AllowAppDone") {
        val repository = mockk<ScrobbleSettingRepository>()
        coEvery { repository.allowedAppsFlow() } returns flowOf(setOf("com.spotify.music"))
        coEvery { repository.disallowApp("com.spotify.music") } returns flowOf(Unit)

        val viewModel = ScrobbleSettingViewModel(repository)
        viewModel.changeAppScrobbleState(appName = "Spotify", enable = false)

        viewModel.uiState.value.event shouldBe ScrobbleSettingViewModel.UiEvent.AllowAppDone
      }
    }

    context("repository fails") {
      it("emits AllowAppError") {
        val repository = mockk<ScrobbleSettingRepository>()
        coEvery { repository.allowedAppsFlow() } returns flowOf(emptySet())
        coEvery { repository.allowApp("com.spotify.music") } returns flow { throw RuntimeException("boom") }

        val viewModel = ScrobbleSettingViewModel(repository)
        viewModel.changeAppScrobbleState(appName = "Spotify", enable = true)

        viewModel.uiState.value.event shouldBe ScrobbleSettingViewModel.UiEvent.AllowAppError
      }
    }
  }

  describe("#popEvent") {
    it("clears the current event") {
      val repository = mockk<ScrobbleSettingRepository>()
      coEvery { repository.allowedAppsFlow() } returns flowOf(emptySet())
      coEvery { repository.allowApp("com.spotify.music") } returns flowOf(Unit)

      val viewModel = ScrobbleSettingViewModel(repository)
      viewModel.changeAppScrobbleState(appName = "Spotify", enable = true)
      viewModel.popEvent()

      viewModel.uiState.value.event shouldBe null
    }
  }
})
