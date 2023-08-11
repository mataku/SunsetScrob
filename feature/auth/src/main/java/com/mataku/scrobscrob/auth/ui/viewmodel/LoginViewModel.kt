package com.mataku.scrobscrob.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: SessionRepository) : ViewModel() {

  var uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.initialize())
    private set

  fun popEvent() {
    uiState.update {
      it.copy(
        event = null
      )
    }
  }

  fun authorize(username: String, password: String) {
    if (username.isBlank()) {
      uiState.update {
        it.copy(
          event = UiEvent.EmptyUsernameError
        )
      }
      return
    }

    if (password.isBlank()) {
      uiState.update {
        it.copy(event = UiEvent.EmptyPasswordError)
      }
      return
    }

    viewModelScope.launch {
      repo.authorize(
        userName = username,
        password = password
      )
        .onStart {
          uiState.update {
            it.copy(
              isLoading = true
            )
          }
        }
        .onCompletion {
          uiState.update {
            it.copy(
              isLoading = false
            )
          }
        }
        .catch {
          uiState.update { state ->
            state.copy(
              event = UiEvent.LoginFailed
            )
          }
        }
        .collect {
          uiState.update { state ->
            state.copy(isLoading = false, event = UiEvent.LoginSuccess)
          }
        }
    }
  }

  fun updateUsername(username: String) {
    uiState.update {
      it.copy(username = username)
    }
  }

  fun updatePassword(password: String) {
    uiState.update {
      it.copy(password = password)
    }
  }

  data class LoginUiState(
    val isLoading: Boolean,
    val event: UiEvent? = null,
    val username: String,
    val password: String
  ) {
    companion object {
      fun initialize() = LoginUiState(
        isLoading = false,
        event = null,
        username = "",
        password = ""
      )
    }
  }

  sealed class UiEvent {
    object LoginSuccess : UiEvent()
    object LoginFailed : UiEvent()
    object EmptyUsernameError : UiEvent()
    object EmptyPasswordError : UiEvent()
  }
}
