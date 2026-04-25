package com.mataku.scrobscrob.auth.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.data.repository.SessionRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class LoginViewModel(private val repo: SessionRepository) : ViewModel() {

  val uiState: StateFlow<LoginUiState>
    field = MutableStateFlow(LoginUiState.initialize())

  fun popEvent(event: UiEvent) {
    val newEvents = uiState.value.events.filterNot {
      it == event
    }.toImmutableList()
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
          events = (it.events + UiEvent.EmptyUsernameError).toImmutableList()
        )
      }
      return
    }

    if (password.isBlank()) {
      uiState.update {
        it.copy(events = (it.events + UiEvent.EmptyPasswordError).toImmutableList())
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
              events = (state.events + UiEvent.LoginFailed).toImmutableList()
            )
          }
        }
        .collect {
          uiState.update { state ->
            state.copy(
              isLoading = false,
              events = (state.events + UiEvent.LoginSuccess).toImmutableList()
            )
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

  @Immutable
  data class LoginUiState(
    val isLoading: Boolean,
    val events: ImmutableList<UiEvent>,
    val username: String,
    val password: String
  ) {
    companion object {
      fun initialize() = LoginUiState(
        isLoading = false,
        events = persistentListOf(),
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
