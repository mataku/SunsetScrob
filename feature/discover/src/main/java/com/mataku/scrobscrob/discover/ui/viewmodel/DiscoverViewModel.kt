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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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
      val lovedTracksFlow = userRepository.getLovedTracks(requestPage)
        .catch { emit(emptyList()) }
      val topTracksFlow = chartRepository.topTracks(requestPage)
        .map { it.topTracks }
        .catch { emit(persistentListOf()) }
      val topArtistsFlow = chartRepository.topArtists(requestPage)
        .map { it.topArtists }
        .catch { emit(persistentListOf()) }

      combine(
        lovedTracksFlow,
        topTracksFlow,
        topArtistsFlow,
      ) { lovedTracks, topTracks, topArtists ->
        DiscoverUiState(
          recentlyLovedTracks = lovedTracks.toImmutableList(),
          topTracks = topTracks,
          topArtists = topArtists,
        )
      }.collect { newState ->
        uiState.value = newState
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
