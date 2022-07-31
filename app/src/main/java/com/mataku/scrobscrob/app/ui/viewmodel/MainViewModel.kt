package com.mataku.scrobscrob.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    private var _theme: MutableStateFlow<AppTheme?> = MutableStateFlow(null)
    val theme: StateFlow<AppTheme?> = _theme.asStateFlow()

    init {
        viewModelScope.launch {
            themeRepository.currentTheme()
                .catch {
                    _theme.value = AppTheme.DARK
                }
                .collect {
                    _theme.value = it
                }

        }
    }
}