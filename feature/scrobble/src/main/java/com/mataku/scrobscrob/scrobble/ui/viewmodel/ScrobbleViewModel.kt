package com.mataku.scrobscrob.scrobble.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
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
class ScrobbleViewModel @Inject constructor(
  private val scrobbleRepository: ScrobbleRepository
) : ViewModel() {

  var uiState = MutableStateFlow(ScrobbleUiState.initialize())
    private set

  private var page = 1

  init {
    fetchRecentTracks()
  }

  fun refresh() {
    if (uiState.value.isLoading || uiState.value.isRefreshing) return

    page = 1

    viewModelScope.launch {
      uiState.update {
        it.copy(
          isRefreshing = true
        )
      }
      scrobbleRepository.recentTracks(
        page = page
      ).catch { e ->
        uiState.update { state ->
          state.copy(
            isLoading = false,
            hasNext = false,
            uiEvents = (state.uiEvents.toMutableList() + ScrobbleUiEvent.Error(e)).toImmutableList(),
          )
        }
      }.onStart {
        uiState.update {
          it.copy(
            isRefreshing = true
          )
        }
      }.onCompletion {
        uiState.update {
          it.copy(
            isRefreshing = false
          )
        }
      }.collect { recentTracks ->
        val currentList = uiState.value.recentTracks
        if (currentList.isEmpty() || recentTracks.isEmpty()) {
          uiState.update { state ->
            state.copy(
              isRefreshing = false,
              recentTracks = recentTracks.toImmutableList(),
              hasNext = recentTracks.isNotEmpty()
            )
          }
        } else {
          val currentFirst = currentList.first()
          val recentFirst = recentTracks.first()
          if (currentFirst.date != recentFirst.date) {
            uiState.update { state ->
              state.copy(
                isRefreshing = false,
                recentTracks = recentTracks.toImmutableList(),
                hasNext = recentTracks.isNotEmpty()
              )
            }
          }
        }
        page++
      }
    }
  }

  fun fetchRecentTracks() {
    if (!uiState.value.hasNext || uiState.value.isLoading || uiState.value.isRefreshing) return
    viewModelScope.launch {
      scrobbleRepository.recentTracks(
        page = page
      ).catch { e ->
        uiState.update { state ->
          state.copy(
            isLoading = false,
            uiEvents = (state.uiEvents.toMutableList() + ScrobbleUiEvent.Error(e)).toImmutableList(),
            hasNext = false
          )
        }
      }.onStart {
        uiState.update {
          it.copy(
            isLoading = true
          )
        }
      }.onCompletion {
        uiState.update {
          it.copy(
            isLoading = false
          )
        }
      }.collect {
        val recentTracks = uiState.value.recentTracks.toMutableList()
        recentTracks.addAll(it)
        uiState.update { state ->
          state.copy(
            isLoading = false,
            recentTracks = recentTracks.toImmutableList(),
            hasNext = it.isNotEmpty()
          )
        }
        page++
      }
    }
  }

  @Immutable
  data class ScrobbleUiState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val uiEvents: ImmutableList<ScrobbleUiEvent>,
    val recentTracks: ImmutableList<RecentTrack>,
    val hasNext: Boolean = true
  ) {
    companion object {
      fun initialize(): ScrobbleUiState {
        return ScrobbleUiState(
          isLoading = false,
          isRefreshing = false,
          recentTracks = persistentListOf(),
          uiEvents = persistentListOf()
        )
      }
    }
  }

  @Immutable
  sealed class ScrobbleUiEvent {
    data class Error(val throwable: Throwable) : ScrobbleUiEvent()
  }
}
