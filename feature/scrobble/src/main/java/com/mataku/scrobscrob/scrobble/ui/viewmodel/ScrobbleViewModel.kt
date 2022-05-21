package com.mataku.scrobscrob.scrobble.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.core.entity.presentation.onFailure
import com.mataku.scrobscrob.core.entity.presentation.onSuccess
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScrobbleViewModel @Inject constructor(
    private val scrobbleRepository: ScrobbleRepository
) : ViewModel() {

    var uiState = MutableStateFlow<UiState>(UiState.initialize())
        private set

    private var page = 1

    init {
        fetchRecentTracks()
    }

    fun fetchRecentTracks() {
        if (!uiState.value.hasNext) return
        viewModelScope.launch {
            uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val result = scrobbleRepository.recentTracks(
                page = page
            )
            result
                .onSuccess {
                    uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            recentTracks = state.recentTracks + it,
                            hasNext = state.recentTracks.isNotEmpty()
                        )
                    }
                    page++
                }
                .onFailure {
                    uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            throwable = it,
                            hasNext = false
                        )
                    }
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