package com.mataku.scrobscrob.auth.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthResult
import com.mataku.scrobscrob.auth.webauth.WebAuthCallbackChannel
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class LoginViewModel(
  private val repo: SessionRepository,
  webAuthCallback: WebAuthCallbackChannel,
) : ViewModel() {

  val uiState: StateFlow<LoginUiState>
    field = MutableStateFlow(LoginUiState.initialize())

  init {
    repo.webAuthUrl()
      .onEach { url ->
        uiState.update { it.copy(webAuthUrl = url) }
      }
      .launchIn(viewModelScope)

    webAuthCallback.tokens
      .onEach { token -> authorize(token) }
      .launchIn(viewModelScope)
  }

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

  fun onWebAuthResult(result: LastFmWebAuthResult) {
    when (result) {
      is LastFmWebAuthResult.Success -> authorize(result.token)
      LastFmWebAuthResult.Canceled -> Unit
      LastFmWebAuthResult.Failed -> uiState.update { state ->
        state.copy(events = (state.events + UiEvent.LoginFailed).toImmutableList())
      }
    }
  }

  fun authorize(token: String) {
    viewModelScope.launch {
      repo.authorize(token)
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

  @Immutable
  data class LoginUiState(
    val isLoading: Boolean,
    val webAuthUrl: String?,
    val events: ImmutableList<UiEvent>,
  ) {
    companion object {
      fun initialize() = LoginUiState(
        isLoading = false,
        webAuthUrl = null,
        events = persistentListOf(),
      )
    }
  }

  sealed class UiEvent {
    object LoginSuccess : UiEvent()
    object LoginFailed : UiEvent()
  }
}
