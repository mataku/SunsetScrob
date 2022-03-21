package com.mataku.scrobscrob.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.api.endpoint.Artist
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopArtistsViewModel @Inject constructor(
    private val topArtistsRepository: TopArtistsRepository,
    usernameRepository: UsernameRepository
) : ViewModel() {

    private val username: String = usernameRepository.username() ?: ""

    var uiState by mutableStateOf(UiState.initialize())
        private set

    private var page = 1

    init {
        if (username.isBlank()) {
            uiState = uiState.copy(
                isLoading = false,
                hasNext = false
            )
        } else {
            fetchTopArtists()
        }
    }

    fun fetchTopArtists() {
        if (uiState.isLoading) {
            return
        }

        viewModelScope.launch {
            topArtistsRepository.fetchTopArtists(page = page, username = username)
                .onStart {
                    uiState = uiState.copy(
                        isLoading = true
                    )
                }
                .onCompletion {
                    uiState = uiState.copy(
                        isLoading = false
                    )
                }
                .catch {
                    uiState = uiState.copy(
                        hasNext = false
                    )
                }
                .collect {
                    if (it.isEmpty()) {
                        uiState = uiState.copy(
                            hasNext = false
                        )
                    } else {
                        val artists = uiState.topArtists.toMutableList()
                        artists.addAll(it)
                        uiState = uiState.copy(
                            topArtists = artists
                        )
                        page++
                    }
                }
        }
    }

    data class UiState(
        val isLoading: Boolean,
        val topArtists: List<Artist>,
        val hasNext: Boolean
    ) {
        companion object {
            fun initialize(): UiState =
                UiState(
                    isLoading = false,
                    topArtists = emptyList(),
                    hasNext = true
                )
        }
    }
}