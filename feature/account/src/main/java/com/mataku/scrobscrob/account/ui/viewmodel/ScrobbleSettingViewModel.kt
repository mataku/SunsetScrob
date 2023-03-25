package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.account.ui.screen.mappedApp
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ScrobbleSettingViewModel(
  private val scrobbleSettingRepository: ScrobbleSettingRepository
) : ViewModel() {

  var uiState by mutableStateOf(UiState())
    private set

  init {
    viewModelScope.launch {
      scrobbleSettingRepository.allowedAppsFlow()
        .onStart {
          uiState = uiState.copy(isLoading = true)
        }.catch {

        }
        .distinctUntilChanged()
        .collect {
          uiState = uiState.copy(
            allowedApps = it,
            isLoading = false
          )
        }
    }
  }

  fun changeAppScrobbleState(appName: String, enable: Boolean) {
    val packageName = appName.mappedApp() ?: return

    viewModelScope.launch {
      val request = if (enable) {
        scrobbleSettingRepository.allowApp(packageName)
      } else {
        scrobbleSettingRepository.disallowApp(packageName)
      }
      request
        .onStart {
          uiState = uiState.copy(isLoading = true)
        }
        .onCompletion {
          uiState = uiState.copy(isLoading = false)
        }.catch {
          uiState = uiState.copy(event = UiEvent.AllowAppError)
        }.collect {
          uiState = uiState.copy(
            event = UiEvent.AllowAppDone,
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
