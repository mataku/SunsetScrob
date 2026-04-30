package com.mataku.scrobscrob.app.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ThemeRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class MainViewModel(
  private val themeRepository: ThemeRepository,
  private val usernameRepository: UsernameRepository,
) : ViewModel() {

  val state: StateFlow<MainUiState?>
    field = MutableStateFlow(null)

  init {
    viewModelScope.launch {
      combine(
        themeRepository.currentTheme(),
        usernameRepository.usernameFlow(),
      ) { theme, username ->
        MainUiState(theme = theme, username = username)
      }
        .catch {
          state.value = MainUiState(
            theme = AppTheme.DARK,
            username = null
          )
        }
        .collect {
          state.value = it
        }
    }
  }

  @Immutable
  data class MainUiState(
    val theme: AppTheme,
    val username: String?,
  )
}
