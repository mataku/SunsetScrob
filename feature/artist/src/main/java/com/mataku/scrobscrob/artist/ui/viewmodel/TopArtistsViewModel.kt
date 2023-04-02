package com.mataku.scrobscrob.artist.ui.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopArtistsViewModel @Inject constructor(
  private val topArtistsRepository: TopArtistsRepository,
  usernameRepository: UsernameRepository
) : ViewModel() {

  private val username: String = usernameRepository.username() ?: ""

  var uiState = MutableStateFlow(TopArtistsUiState.initialize())
    private set

  private var page: Int = 1

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

  fun fetchTopArtists(timeRangeChanged: Boolean = false) {
    val currentState = uiState.value
    if (currentState.isLoading) {
      return
    }

    if (timeRangeChanged) {
      page = 1
    }

    viewModelScope.launch {
      topArtistsRepository.fetchTopArtists(
        page = page,
        username = username,
        timeRangeFiltering = currentState.selectedTimeRangeFiltering
      )
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
            val list = if (timeRangeChanged) {
              emptyList<ArtistInfo>().toImmutableSet()
            } else {
              currentState.topArtists
            }
            uiState.update { state ->
              state.copy(
                hasNext = false,
                topArtists = list
              )
            }
          } else {
            val artists = if (timeRangeChanged) {
              it
            } else {
              val current = uiState.value.topArtists.toMutableList()
              current.addAll(it)
              current
            }.toImmutableSet()
            uiState.update { state ->
              state.copy(
                topArtists = artists,
              )
            }
            page++
          }
        }
    }
  }

  fun updateTimeRange(filtering: TimeRangeFiltering) {
    if (uiState.value.selectedTimeRangeFiltering == filtering) {
      return
    }

    uiState.update {
      it.copy(
        selectedTimeRangeFiltering = filtering
      )
    }
    fetchTopArtists(timeRangeChanged = true)
  }

  @Stable
  data class TopArtistsUiState(
    val isLoading: Boolean,
    val topArtists: ImmutableSet<ArtistInfo>,
    val hasNext: Boolean,
    val selectedTimeRangeFiltering: TimeRangeFiltering,
  ) {
    companion object {
      fun initialize(): TopArtistsUiState =
        TopArtistsUiState(
          isLoading = false,
          topArtists = emptyList<ArtistInfo>().toImmutableSet(),
          hasNext = true,
          selectedTimeRangeFiltering = TimeRangeFiltering.OVERALL,
        )
    }
  }
}
