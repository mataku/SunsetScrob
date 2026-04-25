package com.mataku.scrobscrob.discover.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.LovedTrack
import com.mataku.scrobscrob.data.repository.ChartRepository
import com.mataku.scrobscrob.data.repository.UserRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class DiscoverViewModel(
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
        userRepository.getLovedTracks(requestPage)
          .catch { }
          .collect { tracks ->
            uiState.update {
              it.copy(recentlyLovedTracks = tracks.toImmutableList())
            }
          }
      }
      launch {
        chartRepository.topTracks(requestPage)
          .catch { }
          .collect { topTracks ->
            uiState.update {
              it.copy(topTracks = topTracks.topTracks.toImmutableList())
            }
          }
      }
      launch {
        chartRepository.topArtists(requestPage)
          .catch { }
          .collect { topArtists ->
            uiState.update {
              it.copy(topArtists = topArtists.topArtists.toImmutableList())
            }
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
