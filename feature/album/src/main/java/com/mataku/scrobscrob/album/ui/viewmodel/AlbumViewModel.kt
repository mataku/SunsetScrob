package com.mataku.scrobscrob.album.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.data.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
  private val albumRepository: AlbumRepository,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val artistName = savedStateHandle.get<String>("artistName")
  private val albumName = savedStateHandle.get<String>("albumName")
  private val artworkUrl = savedStateHandle.get<String>("artworkUrl")

  var uiState: MutableStateFlow<AlbumUiState> = MutableStateFlow(AlbumUiState())

  init {
    if (!artistName.isNullOrEmpty() && !albumName.isNullOrEmpty() && !artworkUrl.isNullOrEmpty()) {
      uiState.update {
        it.copy(
          preloadAlbumName = albumName,
          preloadArtistName = artistName,
          preloadArtworkUrl = artworkUrl
        )
      }
      fetchAlbumInfo(
        artistName = artistName,
        albumName = albumName
      )
    }
  }

  private fun fetchAlbumInfo(
    artistName: String,
    albumName: String
  ) {
    albumRepository.albumInfo(
      albumName = albumName,
      artistName = artistName
    ).catch {

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
    }.onEach { albumInfo ->
      uiState.update {
        it.copy(
          albumInfo = albumInfo
        )
      }
    }.launchIn(viewModelScope)
  }

  data class AlbumUiState(
    val isLoading: Boolean = false,
    val albumInfo: AlbumInfo? = null,
    val preloadArtistName: String = "",
    val preloadAlbumName: String = "",
    val preloadArtworkUrl: String = ""
  )
}
