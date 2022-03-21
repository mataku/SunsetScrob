package com.mataku.scrobscrob.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.core.entity.presentation.onFailure
import com.mataku.scrobscrob.core.entity.presentation.onSuccess
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScrobbleViewModel @Inject constructor(
    private val scrobbleRepository: ScrobbleRepository
) : ViewModel() {

    var uiState by mutableStateOf(UiState.initialize())
        private set

    private var page = 1

    init {
        fetchRecentTracks()
    }

    fun fetchRecentTracks() {
        if (!uiState.hasNext) return
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )
            val result = scrobbleRepository.recentTracks(
                page = page
            )
            result
                .onSuccess {
                    uiState = uiState.copy(
                        isLoading = false,
                        recentTracks = uiState.recentTracks + it,
                        hasNext = it.isNotEmpty()
                    )
                    page++
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        throwable = it,
                        hasNext = false
                    )
                }
        }
    }

    data class UiState(
        val isLoading: Boolean,
        val throwable: Throwable? = null,
        val recentTracks: List<RecentTrack> = emptyList(),
        val hasNext: Boolean = true
    ) {
        companion object {
            fun initialize(): UiState {
                return UiState(
                    isLoading = false,
                    throwable = null,
                    recentTracks = emptyList()
                )
            }
        }
    }
}