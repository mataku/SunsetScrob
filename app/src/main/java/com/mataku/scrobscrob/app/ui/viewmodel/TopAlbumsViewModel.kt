package com.mataku.scrobscrob.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.data.repository.TopAlbumsRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopAlbumsViewModel @Inject constructor(
    private val topAlbumsRepository: TopAlbumsRepository,
    usernameRepository: UsernameRepository
) : ViewModel() {

    var uiState by mutableStateOf(UiState.initialized())
        private set

    private val username: String = usernameRepository.username() ?: ""

    private var page = 1

    init {
        if (username.isBlank()) {
            uiState = uiState.copy(
                isLoading = false,
                hasNext = false
            )
        }
    }

    fun fetchAlbums() {
        if (uiState.isLoading) {
            return
        }

        viewModelScope.launch {
            topAlbumsRepository.fetchTopAlbums(page = page, username = username)
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
                .collect { albums ->
                    if (albums.isEmpty()) {
                        uiState = uiState.copy(
                            hasNext = false
                        )
                    } else {
                        val currentAlbums = uiState.topAlbums.toMutableList()
                        currentAlbums.addAll(albums)
                        uiState = uiState.copy(
                            topAlbums = currentAlbums
                        )
                        page++
                    }
                }
        }
    }

    data class UiState(
        val isLoading: Boolean,
        val topAlbums: List<Album>,
        val hasNext: Boolean
    ) {
        companion object {
            fun initialized(): UiState =
                UiState(
                    isLoading = false,
                    topAlbums = emptyList(),
                    hasNext = true
                )

        }
    }
}