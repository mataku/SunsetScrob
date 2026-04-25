package com.mataku.scrobscrob.auth.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.data.repository.SessionRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class LogoutConfirmationViewModel(
  private val sessionRepository: SessionRepository
) : ViewModel() {

  val uiState: StateFlow<UiState>
    field = MutableStateFlow(UiState.initial)

  fun logout() {
    viewModelScope.launch {
      sessionRepository.logout()
        .catch {

        }
        .onCompletion {
          uiState.update { it.copy(logoutEvent = Unit) }
        }
        .collect { }
    }
  }

  @Immutable
  data class UiState(
    val logoutEvent: Unit? = null
  ) {
    companion object {
      val initial = UiState(logoutEvent = null)
    }
  }
}
