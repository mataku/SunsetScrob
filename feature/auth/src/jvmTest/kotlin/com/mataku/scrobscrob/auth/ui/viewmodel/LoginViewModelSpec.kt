package com.mataku.scrobscrob.auth.ui.viewmodel

import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthResult
import com.mataku.scrobscrob.auth.webauth.WebAuthCallbackChannel
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.TimeoutException

class LoginViewModelSpec : DescribeSpec({

  extension(CoroutinesListener())

  val webAuthUrl = "https://www.last.fm/api/auth/?api_key=key&cb=cb"
  val token = "abc123"

  fun repository(): SessionRepository = mockk<SessionRepository>().also {
    every { it.webAuthUrl() } returns flowOf(webAuthUrl)
  }

  describe("initial state") {
    it("exposes the web auth URL and no events") {
      val viewModel = LoginViewModel(repository(), WebAuthCallbackChannel())
      viewModel.uiState.value.let {
        it.webAuthUrl shouldBe webAuthUrl
        it.isLoading.shouldBeFalse()
        it.events.shouldBeEmpty()
      }
    }
  }

  describe("#popEvent") {
    it("should clear event") {
      val repo = repository()
      coEvery { repo.authorize(token) } returns flowOf(Unit)
      val viewModel = LoginViewModel(repo, WebAuthCallbackChannel())
      viewModel.authorize(token)
      viewModel.popEvent(LoginViewModel.UiEvent.LoginSuccess)
      viewModel.uiState.value.events.shouldBeEmpty()
    }
  }

  describe("#authorize") {
    context("failed to login") {
      it("should emit LoginFailed and stop loading") {
        val repo = repository()
        coEvery { repo.authorize(token) } returns flow { throw TimeoutException() }
        val viewModel = LoginViewModel(repo, WebAuthCallbackChannel())
        viewModel.authorize(token)
        viewModel.uiState.value.let {
          it.events shouldBe listOf(LoginViewModel.UiEvent.LoginFailed)
          it.isLoading.shouldBeFalse()
        }
      }
    }

    context("success") {
      it("should emit LoginSuccess and stop loading") {
        val repo = repository()
        coEvery { repo.authorize(token) } returns flowOf(Unit)
        val viewModel = LoginViewModel(repo, WebAuthCallbackChannel())
        viewModel.authorize(token)
        viewModel.uiState.value.let {
          it.events shouldBe listOf(LoginViewModel.UiEvent.LoginSuccess)
          it.isLoading.shouldBeFalse()
        }
      }
    }
  }

  describe("#onWebAuthResult") {
    it("authorizes with the token on Success") {
      val repo = repository()
      coEvery { repo.authorize(token) } returns flowOf(Unit)
      val viewModel = LoginViewModel(repo, WebAuthCallbackChannel())
      viewModel.onWebAuthResult(LastFmWebAuthResult.Success(token))
      viewModel.uiState.value.events shouldBe listOf(LoginViewModel.UiEvent.LoginSuccess)
      coVerify(exactly = 1) { repo.authorize(token) }
    }

    it("emits LoginFailed on Failed") {
      val repo = repository()
      val viewModel = LoginViewModel(repo, WebAuthCallbackChannel())
      viewModel.onWebAuthResult(LastFmWebAuthResult.Failed)
      viewModel.uiState.value.events shouldBe listOf(LoginViewModel.UiEvent.LoginFailed)
      coVerify(exactly = 0) { repo.authorize(any()) }
    }
  }

  describe("web auth scrim") {
    it("is hidden initially") {
      val viewModel = LoginViewModel(repository(), WebAuthCallbackChannel())
      viewModel.uiState.value.isWebAuthOpen.shouldBeFalse()
    }

    it("shows while the web auth is open") {
      val viewModel = LoginViewModel(repository(), WebAuthCallbackChannel())
      viewModel.onWebAuthOpened()
      viewModel.uiState.value.isWebAuthOpen.shouldBeTrue()
    }

    it("hides when the web auth is closed without a token") {
      val viewModel = LoginViewModel(repository(), WebAuthCallbackChannel())
      viewModel.onWebAuthOpened()
      viewModel.onWebAuthResult(LastFmWebAuthResult.Closed)
      viewModel.uiState.value.let {
        it.isWebAuthOpen.shouldBeFalse()
        it.events.shouldBeEmpty()
      }
    }

    it("hides when the web auth failed to launch") {
      val viewModel = LoginViewModel(repository(), WebAuthCallbackChannel())
      viewModel.onWebAuthOpened()
      viewModel.onWebAuthResult(LastFmWebAuthResult.Failed)
      viewModel.uiState.value.isWebAuthOpen.shouldBeFalse()
    }

    it("hides when the web auth succeeded") {
      val repo = repository()
      coEvery { repo.authorize(token) } returns flowOf(Unit)
      val viewModel = LoginViewModel(repo, WebAuthCallbackChannel())
      viewModel.onWebAuthOpened()
      viewModel.onWebAuthResult(LastFmWebAuthResult.Success(token))
      viewModel.uiState.value.isWebAuthOpen.shouldBeFalse()
    }
  }

  describe("callback channel") {
    it("authorizes with a token delivered through WebAuthCallbackChannel") {
      val repo = repository()
      coEvery { repo.authorize(token) } returns flowOf(Unit)
      val channel = WebAuthCallbackChannel()
      val viewModel = LoginViewModel(repo, channel)
      channel.offer(token)
      viewModel.uiState.value.events shouldBe listOf(LoginViewModel.UiEvent.LoginSuccess)
      coVerify(exactly = 1) { repo.authorize(token) }
    }

    it("authorizes with a token offered before the ViewModel existed") {
      val repo = repository()
      coEvery { repo.authorize(token) } returns flowOf(Unit)
      val channel = WebAuthCallbackChannel()
      channel.offer(token)
      val viewModel = LoginViewModel(repo, channel)
      viewModel.uiState.value.events shouldBe listOf(LoginViewModel.UiEvent.LoginSuccess)
      coVerify(exactly = 1) { repo.authorize(token) }
    }
  }
})
