package com.mataku.scrobscrob.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ThemeRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val themeRepository: ThemeRepository,
  private val usernameRepository: UsernameRepository,
) : ViewModel() {

  private var _state: MutableStateFlow<MainUiState?> = MutableStateFlow(null)
  val state: StateFlow<MainUiState?> = _state.asStateFlow()

  init {
    viewModelScope.launch {
      val username = usernameRepository.asyncUsername()

      themeRepository.currentTheme()
        .catch {
          _state.value = MainUiState(
            theme = AppTheme.DARK,
            username = username
          )
        }
        .collect {
          _state.value = MainUiState(
            theme = it,
            username = username
          )
        }
    }
  }

  data class MainUiState(
    val theme: AppTheme,
    val username: String?,
  )
}
