package com.mataku.scrobscrob.home.ui.viewmodel

import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class HomeViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("init") {
    context("username is set") {
      it("populates the username and emits no event") {
        val usernameRepository = mockk<UsernameRepository>()
        every { usernameRepository.asyncUsername() } returns flowOf("matakucom")

        val viewModel = HomeViewModel(usernameRepository)

        viewModel.uiState.value.username shouldBe "matakucom"
        viewModel.uiState.value.events.shouldBeEmpty()
      }
    }

    context("username is null") {
      it("emits a RedirectToLogin event") {
        val usernameRepository = mockk<UsernameRepository>()
        every { usernameRepository.asyncUsername() } returns flowOf(null)

        val viewModel = HomeViewModel(usernameRepository)

        viewModel.uiState.value.username shouldBe ""
        viewModel.uiState.value.events shouldBe listOf(HomeViewModel.HomeUiEvent.RedirectToLogin)
      }
    }

    context("username is empty") {
      it("emits a RedirectToLogin event") {
        val usernameRepository = mockk<UsernameRepository>()
        every { usernameRepository.asyncUsername() } returns flowOf("")

        val viewModel = HomeViewModel(usernameRepository)

        viewModel.uiState.value.events shouldBe listOf(HomeViewModel.HomeUiEvent.RedirectToLogin)
      }
    }
  }

  describe("#consumeEvent") {
    it("removes the consumed event from UI state") {
      val usernameRepository = mockk<UsernameRepository>()
      every { usernameRepository.asyncUsername() } returns flowOf(null)

      val viewModel = HomeViewModel(usernameRepository)
      viewModel.consumeEvent(HomeViewModel.HomeUiEvent.RedirectToLogin)

      viewModel.uiState.value.events.shouldBeEmpty()
    }
  }
})
