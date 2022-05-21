package com.mataku.scrobscrob.album.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.data.repository.TopAlbumsRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
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
class TopAlbumsViewModel @Inject constructor(
    private val topAlbumsRepository: TopAlbumsRepository,
    usernameRepository: UsernameRepository
) : ViewModel() {

    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.initialized())
        private set

    private val username: String = usernameRepository.username() ?: ""

    private var page = 1

    init {
        if (username.isBlank()) {
            uiState.update {
                it.copy(
                    isLoading = false,
                    hasNext = false
                )
            }
        } else {
            fetchAlbums()
        }
    }

    fun fetchAlbums() {
        if (uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {
            topAlbumsRepository.fetchTopAlbums(page = page, username = username)
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
                    uiState.update {
                        it.copy(
                            hasNext = false
                        )
                    }
                }
                .collect { albums ->
                    if (albums.isEmpty()) {
                        uiState.update {
                            it.copy(
                                hasNext = false
                            )
                        }
                    } else {
                        val currentAlbums = uiState.value.topAlbums.toMutableList()
                        currentAlbums.addAll(albums)
                        uiState.update {
                            it.copy(
                                topAlbums = currentAlbums
                            )
                        }
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