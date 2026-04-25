package com.mataku.scrobscrob.home.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class HomeViewModel(
  private val usernameRepository: UsernameRepository,
) : ViewModel() {

  var uiState = MutableStateFlow(HomeUiState(username = ""))
    private set

  init {
    Timber.d("MATAKUDEBUG home init")
    viewModelScope.launch {
      val username = usernameRepository.asyncUsername().first()
      if (username.isNullOrEmpty()) {
        uiState.update {
          it.copy(
            events = (it.events + HomeUiEvent.RedirectToLogin).toImmutableList()
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
    }.toImmutableList()

    uiState.update {
      it.copy(
        events = newEvents
      )
    }
  }

  @Immutable
  data class HomeUiState(
    val username: String,
    val events: ImmutableList<HomeUiEvent> = persistentListOf()
  )

  sealed class HomeUiEvent {
    data object RedirectToLogin : HomeUiEvent()
  }
}
