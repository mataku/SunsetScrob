package com.mataku.scrobscrob.artist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TopArtistsViewModel(
  private val topArtistsRepository: TopArtistsRepository,
  usernameRepository: UsernameRepository
) : ViewModel() {

  private val username: String = usernameRepository.username() ?: ""

  var uiState = MutableStateFlow(UiState.initialize())
    private set

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
      fetchTopArtists()
    }
  }

  fun fetchTopArtists() {
    if (uiState.value.isLoading) {
      return
    }

    viewModelScope.launch {
      topArtistsRepository.fetchTopArtists(page = page, username = username)
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
        .collect {
          if (it.isEmpty()) {
            uiState.update { state ->
              state.copy(
                hasNext = false
              )
            }
          } else {
            val artists = uiState.value.topArtists.toMutableList()
            artists.addAll(it)
            uiState.update { state ->
              state.copy(
                topArtists = artists
              )
            }
            page++
          }
        }
    }
  }

  data class UiState(
    val isLoading: Boolean,
    val topArtists: List<ArtistInfo>,
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
