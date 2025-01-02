package com.mataku.scrobscrob.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val usernameRepository: UsernameRepository,
) : ViewModel() {

  var uiState = MutableStateFlow(HomeUiState(username = ""))
    private set

  init {
    Timber.d("MATAKUDEBUG home init")
    viewModelScope.launch {
      val username = usernameRepository.asyncUsername()
      if (username.isNullOrEmpty()) {
        uiState.update {
          it.copy(
            events = it.events + HomeUiEvent.RedirectToLogin
          )
        }
      } else {
        uiState.update {
          it.copy(
            username = username
          )
        }
      }
    }
  }

  fun consumeEvent(event: HomeUiEvent) {
    val newEvents = uiState.value.events.filterNot {
      event == it
    }

    uiState.update {
      it.copy(
        events = newEvents
      )
    }
  }

  data class HomeUiState(
    val username: String,
    val events: List<HomeUiEvent> = emptyList()
  )

  sealed class HomeUiEvent {
    data object RedirectToLogin : HomeUiEvent()
  }
}
