package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ThemeRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class ThemeSelectorViewModel(
  private val themeRepository: ThemeRepository
) : ViewModel() {

  val uiState: StateFlow<ThemeSelectorUiState>
    field = MutableStateFlow(ThemeSelectorUiState.initialize())

  init {
    viewModelScope.launch {
      themeRepository.currentTheme()
        .distinctUntilChanged()
        .catch {
          uiState.update {
            it.copy(theme = AppTheme.DARK)
          }
        }
        .collect { theme ->
          uiState.update {
            it.copy(
              theme = theme
            )
          }
        }
    }
  }

  fun changeTheme(theme: AppTheme) {
    viewModelScope.launch {
      themeRepository.storeTheme(theme)
        .catch {

        }
        .collect {
          uiState.update {
            it.copy(event = UiEvent.ThemeChanged(theme))
          }
        }
    }
  }

  fun popEvent() {
    uiState.update {
      it.copy(event = null)
    }
  }

  @Immutable
  data class ThemeSelectorUiState(
    val theme: AppTheme?,
    val event: UiEvent?
  ) {
    companion object {
      fun initialize() = ThemeSelectorUiState(
        theme = null,
        event = null
      )
    }
  }

  sealed class UiEvent {
    data class ThemeChanged(val theme: AppTheme) : UiEvent()
  }
}
