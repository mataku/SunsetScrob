package com.mataku.scrobscrob.artist.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class TopArtistsViewModel(
  private val topArtistsRepository: TopArtistsRepository,
  usernameRepository: UsernameRepository
) : ViewModel() {

  private val username: String = usernameRepository.username() ?: ""

  val uiState: StateFlow<TopArtistsUiState>
    field = MutableStateFlow(TopArtistsUiState.initialize())

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
        .collect { result ->
          val fetched = result.artists
          val totalPages = result.pagingAttr.totalPages.toIntOrNull() ?: 0
          val hasNext = fetched.isNotEmpty() && page < totalPages

          if (fetched.isEmpty()) {
            val list = if (timeRangeChanged) {
              emptyList<TopArtistInfo>().toImmutableList()
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
              fetched
            } else {
              val current = uiState.value.topArtists.toMutableList()
              current.addAll(fetched)
              current.toImmutableList()
            }
            uiState.update { state ->
              state.copy(
                topArtists = artists,
                hasNext = hasNext
              )
            }
            if (hasNext) page++
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

  @Immutable
  data class TopArtistsUiState(
    val isLoading: Boolean,
    val topArtists: ImmutableList<TopArtistInfo>,
    val hasNext: Boolean,
    val selectedTimeRangeFiltering: TimeRangeFiltering,
  ) {
    companion object {
      fun initialize(): TopArtistsUiState =
        TopArtistsUiState(
          isLoading = false,
          topArtists = persistentListOf(),
          hasNext = true,
          selectedTimeRangeFiltering = TimeRangeFiltering.OVERALL,
        )
    }
  }
}
