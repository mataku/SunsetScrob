package com.mataku.scrobscrob.account.ui.viewmodel

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ThemeRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class ThemeSelectorViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("init") {
    it("collects the current theme into UI state") {
      val themeRepository = mockk<ThemeRepository>()
      coEvery { themeRepository.currentTheme() } returns flowOf(AppTheme.LASTFM_DARK)

      val viewModel = ThemeSelectorViewModel(themeRepository)
      viewModel.uiState.value.theme shouldBe AppTheme.LASTFM_DARK
    }

    it("falls back to AppTheme.DARK when the theme flow fails") {
      val themeRepository = mockk<ThemeRepository>()
      coEvery { themeRepository.currentTheme() } returns flow { throw RuntimeException("boom") }

      val viewModel = ThemeSelectorViewModel(themeRepository)
      viewModel.uiState.value.theme shouldBe AppTheme.DARK
    }
  }

  describe("#changeTheme") {
    it("stores the theme via the repository and emits a ThemeChanged event") {
      val themeRepository = mockk<ThemeRepository>()
      coEvery { themeRepository.currentTheme() } returns flowOf(AppTheme.DARK)
      coEvery { themeRepository.storeTheme(AppTheme.OCEAN) } returns flowOf(Unit)

      val viewModel = ThemeSelectorViewModel(themeRepository)
      viewModel.changeTheme(AppTheme.OCEAN)

      viewModel.uiState.value.event shouldBe ThemeSelectorViewModel.UiEvent.ThemeChanged(AppTheme.OCEAN)
    }
  }

  describe("#popEvent") {
    it("clears the current event") {
      val themeRepository = mockk<ThemeRepository>()
      coEvery { themeRepository.currentTheme() } returns flowOf(AppTheme.DARK)
      coEvery { themeRepository.storeTheme(AppTheme.OCEAN) } returns flowOf(Unit)

      val viewModel = ThemeSelectorViewModel(themeRepository)
      viewModel.changeTheme(AppTheme.OCEAN)
      viewModel.popEvent()

      viewModel.uiState.value.event shouldBe null
    }
  }
})
