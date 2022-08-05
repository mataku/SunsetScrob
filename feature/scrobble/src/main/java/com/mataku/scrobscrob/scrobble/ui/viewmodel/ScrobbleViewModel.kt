package com.mataku.scrobscrob.scrobble.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

  var uiState = MutableStateFlow<UiState>(UiState.initialize())
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
      ).catch {
        uiState.update { state ->
          state.copy(
            isLoading = false,
            throwable = it,
            hasNext = false
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
      }.collect {
        uiState.update { state ->
          state.copy(
            isRefreshing = false,
            recentTracks = it,
            hasNext = it.isNotEmpty()
          )
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
      ).catch {
        uiState.update { state ->
          state.copy(
            isLoading = false,
            throwable = it,
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
        uiState.update { state ->
          state.copy(
            isLoading = false,
            recentTracks = state.recentTracks + it,
            hasNext = it.isNotEmpty()
          )
        }
        page++
      }
    }
  }

  data class UiState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val throwable: Throwable? = null,
    val recentTracks: List<RecentTrack> = emptyList(),
    val hasNext: Boolean = true
  ) {
    companion object {
      fun initialize(): UiState {
        return UiState(
          isLoading = false,
          isRefreshing = false,
          throwable = null,
          recentTracks = emptyList()
        )
      }
    }
  }
}
