package com.mataku.scrobscrob.discover.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.LovedTrack
import com.mataku.scrobscrob.data.repository.ChartRepository
import com.mataku.scrobscrob.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
  private val chartRepository: ChartRepository,
  private val userRepository: UserRepository,
) : ViewModel() {

  var uiState = MutableStateFlow(DiscoverUiState())
    private set

  private val requestPage = 1

  init {
    fetchInitial()
  }

  private fun fetchInitial() {
    viewModelScope.launch {
      launch {
        val recentlyLovedTracks = userRepository.getLovedTracks(requestPage)
        uiState.update {
          it.copy(
            recentlyLovedTracks = recentlyLovedTracks.getOrNull()?.toImmutableList()
              ?: persistentListOf()
          )
        }
      }
      launch {
        val topTracks = chartRepository.topTracksAsync(requestPage)
        uiState.update {
          it.copy(
            topTracks = topTracks.getOrNull()?.topTracks?.toImmutableList() ?: persistentListOf()
          )
        }
      }
      launch {
        val topArtists = chartRepository.topArtistsAsync(requestPage)
        uiState.update {
          it.copy(
            topArtists = topArtists.getOrNull()?.topArtists?.toImmutableList() ?: persistentListOf()
          )
        }
      }
    }
  }

  @Immutable
  data class DiscoverUiState(
    val isLoading: Boolean = false,
    val topArtists: ImmutableList<ChartArtist> = persistentListOf(),
    val topTracks: ImmutableList<ChartTrack> = persistentListOf(),
    val recentlyLovedTracks: ImmutableList<LovedTrack> = persistentListOf(),
  )
}
