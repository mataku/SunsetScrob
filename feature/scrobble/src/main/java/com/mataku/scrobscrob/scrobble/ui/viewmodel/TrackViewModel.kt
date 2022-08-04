package com.mataku.scrobscrob.scrobble.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.api.endpoint.TrackInfo
import com.mataku.scrobscrob.data.repository.TrackRepository
import com.mataku.scrobscrob.scrobble.ui.state.TrackScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
  private val trackRepository: TrackRepository
) : ViewModel() {

  var state = MutableStateFlow<UiState>(UiState.initialize())
    private set

  fun fetchTrackInfo(
    trackName: String,
    artistName: String
  ) {
    viewModelScope.launch {
      trackRepository.getInfo(
        trackName = trackName,
        artistName = artistName
      ).onStart {
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
            event = TrackScreenState.UiEvent.TrackInfoFetchFailure
          )
        }
      }.collect { trackInfo ->
        state.update {
          it.copy(
            trackInfo = trackInfo
          )
        }
      }
    }
  }

  fun clearState() {
    state.update {
      it.copy(
        trackInfo = null
      )
    }
  }

  data class UiState(
    val isLoading: Boolean,
    val trackInfo: TrackInfo?,
    val event: TrackScreenState.UiEvent?
  ) {
    companion object {
      fun initialize(): UiState =
        UiState(
          isLoading = true,
          trackInfo = null,
          event = null
        )
    }
  }
}
