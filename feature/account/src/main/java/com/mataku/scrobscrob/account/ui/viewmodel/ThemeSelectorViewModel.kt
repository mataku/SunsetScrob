package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSelectorViewModel @Inject constructor(
  private val themeRepository: ThemeRepository
) : ViewModel() {

  var uiState: MutableStateFlow<ThemeSelectorUiState> =
    MutableStateFlow(ThemeSelectorUiState.initialize())
    private set

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
