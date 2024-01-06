package com.mataku.scrobscrob.chart.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.chart.ui.ChartType
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.ChartTrack
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
class ChartViewModel @Inject constructor(
  private val chartRepository: ChartRepository
) : ViewModel() {

  var uiState = MutableStateFlow(ChartUiState())
    private set

  private var artistPage = 1
  private var trackPage = 1

  init {
    fetchInitial()
  }

  fun fetchInitial() {
    chartRepository.topArtists(artistPage)
      .zip(chartRepository.topTracks(trackPage)) { artists, tracks ->
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
            hasMoreTopArtists = artists.isNotEmpty(),
            topTracks = tracks.toImmutableList(),
            hasMoreTopTracks = tracks.isNotEmpty()
          )
        }
        artistPage += 1
        trackPage += 1
      }
      .launchIn(viewModelScope)
  }

  fun changeSelectedChartType(chartType: ChartType) {
    val currentState = uiState.value
    if (currentState.chartType == chartType) {
      return
    }
  }

  @Immutable
  data class ChartUiState(
    val isLoading: Boolean = false,
    val topArtists: ImmutableList<ChartArtist> = persistentListOf(),
    val topTracks: ImmutableList<ChartTrack> = persistentListOf(),
    val chartType: ChartType = ChartType.TOP_ARTISTS,
    private val hasMoreTopArtists: Boolean = true,
    private val hasMoreTopTracks: Boolean = true,
  ) {
    fun hasNext(): Boolean {
      return when (chartType) {
        ChartType.TOP_ARTISTS -> hasMoreTopArtists
        ChartType.TOP_TRACKS -> hasMoreTopTracks
      }
    }
  }
}
