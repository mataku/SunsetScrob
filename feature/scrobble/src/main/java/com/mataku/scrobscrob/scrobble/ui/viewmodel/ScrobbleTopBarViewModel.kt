package com.mataku.scrobscrob.scrobble.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScrobbleTopBarViewModel @Inject constructor(
  private val sessionRepository: SessionRepository
) : ViewModel() {

  var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.initial)
    private set

  fun logout() {
    viewModelScope.launch {
      sessionRepository.logout()
        .catch {

        }
        .onCompletion {
          uiState.update {
            it.copy(
              logoutEvent = Unit
            )
          }
        }
        .collect { }
    }
  }

  data class UiState(
    val logoutEvent: Unit? = null
  ) {
    companion object {
      val initial = UiState(logoutEvent = null)
    }
  }
}
