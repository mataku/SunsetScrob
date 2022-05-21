package com.mataku.scrobscrob.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: SessionRepository) : ViewModel() {

    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.initialize())
        private set

    fun authorize(username: String, password: String) {
        viewModelScope.launch {
            repo.authorize(
                userName = username,
                password = password
            )
                .onStart {
                    uiState.update {
                        it.copy(
                            isLoading = true,
                            throwable = null
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
                            throwable = it
                        )
                    }
                }
                .collect {
                    uiState.update {
                        it.copy(isLoading = false, loginEvent = Unit)
                    }
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