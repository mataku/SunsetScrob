package com.mataku.scrobscrob.auth.ui.viewmodel

import com.mataku.scrobscrob.auth.ui.state.LoginScreenState
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.test_helper.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.TimeoutException

class LoginViewModelSpec : DescribeSpec({

  extension(CoroutinesListener())

  val sessionRepository = mockk<SessionRepository>()
  val viewModel = LoginViewModel(sessionRepository)

  describe("#popEvent") {
    it("should clear event") {
      viewModel.popEvent()
      viewModel.uiState.value.event.shouldBeNull()
    }
  }

  describe("#authorize") {
    context("username is blank") {
      it("should return LoginScreenState.UiEvent.EmptyUsernameError") {
        viewModel.authorize(username = "", password = "password")
        viewModel.uiState.value.event shouldBe LoginScreenState.UiEvent.EmptyUsernameError
      }
    }

    context("password is blank") {
      it("should return LoginScreenState.UiEvent.EmptyPasswordError") {
        viewModel.authorize(username = "username", password = "")
        viewModel.uiState.value.let {
          it.event shouldBe LoginScreenState.UiEvent.EmptyPasswordError
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
        viewModel.authorize(username, password)
        viewModel.uiState.value.let {
          it.event shouldBe LoginScreenState.UiEvent.LoginFailed
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
        viewModel.authorize(username, password)
        viewModel.uiState.value.let {
          it.event shouldBe LoginScreenState.UiEvent.LoginSuccess
          it.isLoading.shouldBeFalse()
        }
      }
    }
  }

  describe("#updateUsername") {
    it("should update username") {
      val updatedUsername = "updated"
      viewModel.updateUsername(updatedUsername)
      viewModel.uiState.value.username.shouldBe(updatedUsername)
    }
  }

  describe("#updatePassword") {
    it("should update password") {
      val updatedPassword = "updated"
      viewModel.updatePassword(updatedPassword)
      viewModel.uiState.value.password.shouldBe(updatedPassword)
    }
  }
})
