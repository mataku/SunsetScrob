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

  fun popEvent(event: UiEvent) {
    val newEvents = uiState.value.events.filterNot {
      it == event
    }
    uiState.update {
      it.copy(
        events = newEvents
      )
    }
  }

  fun authorize(username: String, password: String) {
    if (username.isBlank()) {
      uiState.update {
        it.copy(
          events = it.events + UiEvent.EmptyUsernameError
        )
      }
      return
    }

    if (password.isBlank()) {
      uiState.update {
        it.copy(events = it.events + UiEvent.EmptyPasswordError)
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
              events = state.events + UiEvent.LoginFailed
            )
          }
        }
        .collect {
          uiState.update { state ->
            state.copy(isLoading = false, events = state.events + UiEvent.LoginSuccess)
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
    val events: List<UiEvent>,
    val username: String,
    val password: String
  ) {
    companion object {
      fun initialize() = LoginUiState(
        isLoading = false,
        events = emptyList(),
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
