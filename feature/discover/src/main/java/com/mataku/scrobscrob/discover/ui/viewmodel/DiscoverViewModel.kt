package com.mataku.scrobscrob.discover.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.data.repository.ChartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
  private val chartRepository: ChartRepository
) : ViewModel() {

  var uiState = MutableStateFlow(DiscoverUiState())
    private set

  private val requestPage = 1

  init {
    fetchInitial()
  }

  private fun fetchInitial() {
    chartRepository.topArtists(requestPage)
      .zip(chartRepository.topTracks(requestPage)) { artists, tracks ->
        Pair(artists, tracks)
      }
      .catch {

      }
      .onStart {
        uiState.update {
          it.copy(isLoading = true)
        }
      }
      .onCompletion {
        uiState.update {
          it.copy(isLoading = false)
        }
      }
      .onEach {
        uiState.update { state ->
          val artists = it.first.topArtists
          val tracks = it.second.topTracks
          state.copy(
            topArtists = artists.toImmutableList(),
            topTracks = tracks.toImmutableList(),
          )
        }
      }
      .launchIn(viewModelScope)

    chartRepository.topTags(requestPage)
      .catch {

      }
      .onEach {
        uiState.update { state ->
          state.copy(
            topTags = it.toImmutableList()
          )
        }
      }
      .launchIn(viewModelScope)
  }

  @Immutable
  data class DiscoverUiState(
    val isLoading: Boolean = false,
    val topArtists: ImmutableList<ChartArtist> = persistentListOf(),
    val topTracks: ImmutableList<ChartTrack> = persistentListOf(),
    val topTags: ImmutableList<Tag> = persistentListOf()
  )
}
