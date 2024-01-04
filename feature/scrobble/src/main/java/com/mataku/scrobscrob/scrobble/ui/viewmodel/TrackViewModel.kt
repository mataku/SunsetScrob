package com.mataku.scrobscrob.scrobble.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.data.repository.TrackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
  private val trackRepository: TrackRepository,
  private val artistRepository: ArtistRepository,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  var state = MutableStateFlow(TrackUiState.initialize())
    private set

  private val trackName: String? = savedStateHandle["trackName"]
  private val artistName: String? = savedStateHandle["artistName"]

  init {
    fetchTrackInfo(
      trackName = trackName,
      artistName = artistName
    )
  }

  fun fetchTrackInfo(
    trackName: String?,
    artistName: String?
  ) {
    if (trackName == null || artistName == null) {
      return
    }

    viewModelScope.launch {
      trackRepository.getInfo(
        trackName = trackName,
        artistName = artistName
      ).zip(
        artistRepository.artistInfo(artistName)
      ) { track, artist ->
        Pair(track, artist)
      }.onStart {
        state.update {
          it.copy(isLoading = true)
        }
      }.onCompletion {
        state.update {
          it.copy(isLoading = false)
        }
      }.catch {
        state.update {
          it.copy(
            event = UiEvent.TrackInfoFetchFailure
          )
        }
      }.collect { result ->
        state.update {
          it.copy(
            trackInfo = result.first,
            artistInfo = result.second
          )
        }
      }
    }
  }

  @Immutable
  data class TrackUiState(
    val isLoading: Boolean,
    val trackInfo: TrackInfo?,
    val artistInfo: ArtistInfo?,
    val event: UiEvent?
  ) {
    companion object {
      fun initialize(): TrackUiState =
        TrackUiState(
          isLoading = true,
          trackInfo = null,
          event = null,
          artistInfo = null
        )
    }
  }

  sealed class UiEvent {
    object TrackInfoFetchFailure : UiEvent()
  }
}
