package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.account.ui.state.ThemeSelectorState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThemeSelectorViewModel(
  private val themeRepository: ThemeRepository
) : ViewModel() {

  var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.initialize())
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
            it.copy(event = ThemeSelectorState.UiEvent.ThemeChanged(theme))
          }
        }
    }
  }

  fun popEvent() {
    uiState.update {
      it.copy(event = null)
    }
  }

  data class UiState(
    val theme: AppTheme?,
    val event: ThemeSelectorState.UiEvent?
  ) {
    companion object {
      fun initialize() = UiState(
        theme = null,
        event = null
      )
    }
  }
}
