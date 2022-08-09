package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScrobbleSettingViewModel @Inject constructor(
  private val scrobbleSettingRepository: ScrobbleSettingRepository
) : ViewModel() {

  var uiState by mutableStateOf(UiState())
    private set

  init {
    viewModelScope.launch {
      uiState = uiState.copy(isLoading = true)
      val allowedApps = scrobbleSettingRepository.allowedApps()
      uiState = uiState.copy(
        allowedApps = allowedApps,
        isLoading = false
      )
    }
  }

  fun allowApp(appName: String) {
    viewModelScope.launch {
      scrobbleSettingRepository.allowApp(appName)
        .onStart {
          uiState = uiState.copy(isLoading = true)
        }
        .onCompletion {
          uiState = uiState.copy(isLoading = false)
        }.catch {
          uiState = uiState.copy(event = UiEvent.AllowAppError)
        }.collect {
          val apps = uiState.allowedApps
          apps.toMutableSet().add(appName)
          uiState = uiState.copy(
            event = UiEvent.AllowAppDone,
            allowedApps = apps
          )
        }
    }
  }

  fun popEvent() {
    uiState = uiState.copy(event = null)
  }

  data class UiState(
    val allowedApps: Set<String> = emptySet(),
    val isLoading: Boolean = true,
    val event: UiEvent? = null
  )

  sealed class UiEvent {
    object AllowAppDone : UiEvent()
    object AllowAppError : UiEvent()
  }
}
