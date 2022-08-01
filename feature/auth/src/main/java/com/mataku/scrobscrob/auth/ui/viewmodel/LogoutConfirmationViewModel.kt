package com.mataku.scrobscrob.auth.ui.viewmodel

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutConfirmationViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(UiState.initial)
        private set

    fun logout() {
        viewModelScope.launch {
            sessionRepository.logout()
                .catch {

                }
                .onCompletion {
                    uiState = uiState.copy(
                        logoutEvent = Unit
                    )
                }
                .collect { }
        }
    }

    data class UiState(
        val logoutEvent: Unit? = null
    ) {
        companion object {
            val initial = UiState(logoutEvent = null)
        }
    }
}