package com.mataku.scrobscrob.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class TopBarViewModel(
  private val usernameRepository: UsernameRepository
) : ViewModel() {

  var uiState by mutableStateOf(UiState.INITIAL)
    private set

  init {
    viewModelScope.launch {
      usernameRepository.username()
    }
  }

  data class UiState(
    val isLoggedIn: Boolean = false
  ) {
    companion object {
      val INITIAL = UiState(isLoggedIn = false)
    }
  }
}
