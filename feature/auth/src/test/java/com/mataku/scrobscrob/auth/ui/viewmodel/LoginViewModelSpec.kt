package com.mataku.scrobscrob.auth.ui.viewmodel

import com.mataku.scrobscrob.data.repository.SessionRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.TimeoutException

class LoginViewModelSpec : DescribeSpec({

  extension(com.mataku.scrobscrob.test_helper.unit.CoroutinesListener())

  val sessionRepository = mockk<SessionRepository>()

  describe("#popEvent") {
    it("should clear event") {
      val viewModel = LoginViewModel(sessionRepository)
      viewModel.popEvent(LoginViewModel.UiEvent.LoginSuccess)
      viewModel.uiState.value.events.shouldBeEmpty()
    }
  }

  describe("#authorize") {
    context("username is blank") {
      it("should return LoginScreenState.UiEvent.EmptyUsernameError") {
        val viewModel = LoginViewModel(sessionRepository)
        viewModel.authorize(username = "", password = "password")
        viewModel.uiState.value.events shouldBe listOf(LoginViewModel.UiEvent.EmptyUsernameError)
      }
    }

    context("password is blank") {
      it("should return LoginScreenState.UiEvent.EmptyPasswordError") {
        val viewModel = LoginViewModel(sessionRepository)
        viewModel.authorize(username = "username", password = "")
        viewModel.uiState.value.let {
          it.events shouldBe listOf(LoginViewModel.UiEvent.EmptyPasswordError)
          it.isLoading.shouldBeFalse()
        }
      }
    }

    context("failed to login") {
      val username = "username"
      val password = "password"
      coEvery {
        sessionRepository.authorize(
          username, password
        )
      }.returns(flow {
        throw TimeoutException()
      })

      it("should return LoginScreenState.UiEvent.LoginFailed") {
        val viewModel = LoginViewModel(sessionRepository)
        viewModel.authorize(username, password)
        viewModel.uiState.value.let {
          it.events shouldBe listOf(LoginViewModel.UiEvent.LoginFailed)
          it.isLoading.shouldBeFalse()
        }
      }
    }

    context("success") {
      val username = "username"
      val password = "password"
      coEvery {
        sessionRepository.authorize(
          username, password
        )
      }.returns(
        flowOf(Unit)
      )

      it("should return LoginScreenState.UiEvent.LoginSuccess") {
        val viewModel = LoginViewModel(sessionRepository)
        viewModel.authorize(username, password)
        viewModel.uiState.value.let {
          it.events shouldBe listOf(LoginViewModel.UiEvent.LoginSuccess)
          it.isLoading.shouldBeFalse()
        }
      }
    }
  }

  describe("#updateUsername") {
    it("should update username") {
      val updatedUsername = "updated"
      val viewModel = LoginViewModel(sessionRepository)
      viewModel.updateUsername(updatedUsername)
      viewModel.uiState.value.username.shouldBe(updatedUsername)
    }
  }

  describe("#updatePassword") {
    it("should update password") {
      val updatedPassword = "updated"
      val viewModel = LoginViewModel(sessionRepository)
      viewModel.updatePassword(updatedPassword)
      viewModel.uiState.value.password.shouldBe(updatedPassword)
    }
  }
})
