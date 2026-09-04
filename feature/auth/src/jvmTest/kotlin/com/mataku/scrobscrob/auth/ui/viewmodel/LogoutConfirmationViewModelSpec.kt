package com.mataku.scrobscrob.auth.ui.viewmodel

import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class LogoutConfirmationViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  val sessionRepository = mockk<SessionRepository>()

  describe("init") {
    it("starts with no logout event") {
      val viewModel = LogoutConfirmationViewModel(sessionRepository)
      viewModel.uiState.value.logoutEvent shouldBe null
    }
  }

  describe("#logout") {
    context("logout succeeds") {
      it("emits a logoutEvent on completion") {
        coEvery { sessionRepository.logout() } returns flowOf(Unit)

        val viewModel = LogoutConfirmationViewModel(sessionRepository)
        viewModel.logout()

        viewModel.uiState.value.logoutEvent shouldBe Unit
      }
    }

    context("logout fails") {
      it("still emits a logoutEvent on completion (errors are swallowed)") {
        coEvery { sessionRepository.logout() } returns flow { throw RuntimeException("boom") }

        val viewModel = LogoutConfirmationViewModel(sessionRepository)
        viewModel.logout()

        viewModel.uiState.value.logoutEvent shouldBe Unit
      }
    }
  }
})
