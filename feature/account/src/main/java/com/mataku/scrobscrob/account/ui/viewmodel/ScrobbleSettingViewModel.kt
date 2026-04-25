package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.account.ui.screen.mappedApp
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class ScrobbleSettingViewModel(
  private val scrobbleSettingRepository: ScrobbleSettingRepository
) : ViewModel() {

  val uiState: StateFlow<UiState>
    field = MutableStateFlow(UiState())

  init {
    viewModelScope.launch {
      scrobbleSettingRepository.allowedAppsFlow()
        .onStart {
          uiState.update { it.copy(isLoading = true) }
        }.catch {

        }
        .distinctUntilChanged()
        .collect {
          uiState.update { state ->
            state.copy(
              allowedApps = it.toImmutableSet(),
              isLoading = false
            )
          }
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
          uiState.update { it.copy(isLoading = true) }
        }
        .onCompletion {
          uiState.update { it.copy(isLoading = false) }
        }.catch {
          uiState.update { it.copy(event = UiEvent.AllowAppError) }
        }.collect {
          uiState.update { it.copy(event = UiEvent.AllowAppDone) }
        }
    }
  }

  fun popEvent() {
    uiState.update { it.copy(event = null) }
  }

  @Immutable
  data class UiState(
    val allowedApps: ImmutableSet<String> = persistentSetOf(),
    val isLoading: Boolean = true,
    val event: UiEvent? = null
  )

  sealed class UiEvent {
    object AllowAppDone : UiEvent()
    object AllowAppError : UiEvent()
  }
}
