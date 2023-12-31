package com.mataku.scrobscrob.album.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.data.repository.AlbumRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopAlbumsViewModel @Inject constructor(
  private val topAlbumsRepository: AlbumRepository,
  usernameRepository: UsernameRepository
) : ViewModel() {

  var uiState: MutableStateFlow<TopAlbumsUiState> = MutableStateFlow(TopAlbumsUiState.initialized())
    private set

  private val username: String = usernameRepository.username() ?: ""

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
      fetchAlbums()
    }
  }

  fun fetchAlbums(timeRangeFilteringChanged: Boolean = false) {
    val currentState = uiState.value
    if (currentState.isLoading) {
      return
    }

    if (timeRangeFilteringChanged) {
      page = 1
    }

    viewModelScope.launch {
      topAlbumsRepository.fetchTopAlbums(
        page = page,
        username = username,
        timeRangeFiltering = currentState.timeRangeFiltering
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
        .collect { albums ->
          if (albums.isEmpty()) {
            val list = if (timeRangeFilteringChanged) {
              persistentListOf()
            } else {
              currentState.topAlbums
            }
            uiState.update {
              it.copy(
                hasNext = false,
                topAlbums = list
              )
            }
          } else {
            val list = if (timeRangeFilteringChanged) {
              albums
            } else {
              val currentAlbums = uiState.value.topAlbums.toMutableList()
              currentAlbums.addAll(albums)
              currentAlbums
            }.toImmutableList()

            uiState.update {
              it.copy(
                topAlbums = list
              )
            }
            page++
          }
        }
    }
  }

  fun updateTimeRange(selectedTimeRangeFiltering: TimeRangeFiltering) {
    if (uiState.value.timeRangeFiltering == selectedTimeRangeFiltering) {
      return
    }

    uiState.update {
      it.copy(
        timeRangeFiltering = selectedTimeRangeFiltering
      )
    }
    fetchAlbums(timeRangeFilteringChanged = true)
  }

  data class TopAlbumsUiState(
    val isLoading: Boolean,
    val topAlbums: ImmutableList<AlbumInfo>,
    val hasNext: Boolean,
    val timeRangeFiltering: TimeRangeFiltering
  ) {
    companion object {
      fun initialized(): TopAlbumsUiState =
        TopAlbumsUiState(
          isLoading = false,
          topAlbums = persistentListOf(),
          hasNext = true,
          timeRangeFiltering = TimeRangeFiltering.OVERALL
        )
    }
  }
}
