package com.mataku.scrobscrob.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.auth.ui.state.LoginScreenState
import com.mataku.scrobscrob.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: SessionRepository) : ViewModel() {

    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.initialize())
        private set

    fun popEvent() {
        uiState.update {
            it.copy(
                event = null
            )
        }
    }

    fun authorize(username: String, password: String) {
        if (username.isBlank()) {
            uiState.update {
                it.copy(
                    event = LoginScreenState.UiEvent.EmptyUsernameError
                )
            }
            return
        }

        if (password.isBlank()) {
            uiState.update {
                it.copy(event = LoginScreenState.UiEvent.EmptyPasswordError)
            }
            return
        }

        viewModelScope.launch {
            repo.authorize(
                userName = username,
                password = password
            )
                .onStart {
                    uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                .onCompletion {
                    uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
                .catch {
                    uiState.update { state ->
                        state.copy(
                            event = LoginScreenState.UiEvent.LoginFailed
                        )
                    }
                }
                .collect {
                    uiState.update { state ->
                        state.copy(isLoading = false, event = LoginScreenState.UiEvent.LoginSuccess)
                    }
                }
        }
    }

    data class UiState(
        val isLoading: Boolean,
        val event: LoginScreenState.UiEvent? = null
    ) {
        companion object {
            fun initialize() = UiState(
                isLoading = false,
                event = null
            )
        }
    }
}