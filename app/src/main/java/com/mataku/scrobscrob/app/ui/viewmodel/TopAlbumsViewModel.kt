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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopAlbumsViewModel @Inject constructor(
    private val topAlbumsRepository: TopAlbumsRepository,
    private val usernameRepository: UsernameRepository
) : ViewModel() {

    var uiState by mutableStateOf(UiState.initialized())
        private set

    private var username: String = ""

    private var page = 1

    init {
        viewModelScope.launch {
            usernameRepository.username()
                .map {
                    if (it != null) {
                        username = it
                    }
                }
                .catch {

                }
        }
    }

    fun fetchAlbums() {
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
                    isLoading = true,
                    topAlbums = emptyList(),
                    hasNext = true
                )

        }
    }
}