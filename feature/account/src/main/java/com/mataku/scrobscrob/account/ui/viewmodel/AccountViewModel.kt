package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.data.repository.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
  private val themeRepository: ThemeRepository,
  private val sessionRepository: SessionRepository
) : ViewModel() {

  var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.initialize())
    private set

  init {
    viewModelScope.launch {
      themeRepository.currentTheme()
        .catch {
          uiState.update {
            it.copy(
              theme = AppTheme.DARK
            )
          }
        }
        .collect {
          uiState.update { state ->
            state.copy(theme = it)
          }
        }
    }
  }

  fun logout() {
    viewModelScope.launch {
      sessionRepository.logout()
        .catch {

        }
        .onCompletion {
          uiState.update {
            it.copy(event = Event.Logout)
          }
        }
        .collect { }
    }
  }

  fun popEvent() {
    uiState.update {
      it.copy(
        event = null
      )
    }
  }

  data class UiState(
    val theme: AppTheme?,
    val event: Event?
  ) {
    companion object {
      fun initialize() = UiState(
        theme = null,
        event = null
      )
    }
  }

  sealed class Event {
    object Logout : Event()
  }
}
