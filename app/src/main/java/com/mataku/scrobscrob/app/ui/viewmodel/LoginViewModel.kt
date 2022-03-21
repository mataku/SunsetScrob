package com.mataku.scrobscrob.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: SessionRepository) : ViewModel() {

    var uiState by mutableStateOf(UiState.initialize())
        private set

    fun authorize(username: String, password: String) {
        viewModelScope.launch {
            repo.authorize(
                userName = username,
                password = password
            )
                .onStart {
                    uiState = uiState.copy(
                        isLoading = true,
                        throwable = null
                    )
                }
                .onCompletion {
                    uiState = uiState.copy(
                        isLoading = false
                    )
                }
                .catch {
                    uiState = uiState.copy(
                        throwable = it
                    )
                }
                .collect {
                    uiState = UiState(isLoading = false, loginEvent = Unit)
                }
        }
    }

    data class UiState(
        val isLoading: Boolean,
        val throwable: Throwable? = null,
        val loginEvent: Unit? = null
    ) {
        companion object {
            fun initialize() = UiState(
                isLoading = false,
                throwable = null
            )
        }
    }
}