package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.initialize())
        private set

    init {
        viewModelScope.launch {
            themeRepository.currentTheme()
                .catch {
                    uiState.update {
                        it.copy(
                            theme = AppTheme.DARK
                        )
                    }
                }
                .collect {
                    uiState.update { state ->
                        state.copy(theme = it)
                    }
                }
        }
    }

    data class UiState(
        val theme: AppTheme?
    ) {
        companion object {
            fun initialize() = UiState(
                theme = null
            )
        }
    }
}