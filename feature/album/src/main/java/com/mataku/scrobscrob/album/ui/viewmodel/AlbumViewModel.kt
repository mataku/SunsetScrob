package com.mataku.scrobscrob.album.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.data.repository.AlbumRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

@AssistedInject
class AlbumViewModel(
  private val albumRepository: AlbumRepository,
  @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val artistName = savedStateHandle.get<String>("artistName")
  private val albumName = savedStateHandle.get<String>("albumName")
  private val artworkUrl = savedStateHandle.get<String>("artworkUrl") ?: ""

  val uiState: StateFlow<AlbumUiState>
    field = MutableStateFlow(AlbumUiState())

  init {
    if (!artistName.isNullOrEmpty() && !albumName.isNullOrEmpty()) {
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
    ).catch {}.onStart {
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

  @Immutable
  data class AlbumUiState(
    val isLoading: Boolean = false,
    val albumInfo: AlbumInfo? = null,
    val preloadArtistName: String = "",
    val preloadAlbumName: String = "",
    val preloadArtworkUrl: String = ""
  )

  @AssistedFactory
  @ViewModelAssistedFactoryKey(AlbumViewModel::class)
  @ContributesIntoMap(AppScope::class)
  fun interface Factory : ViewModelAssistedFactory {
    override fun create(extras: CreationExtras): AlbumViewModel =
      create(extras.createSavedStateHandle())

    fun create(@Assisted savedStateHandle: SavedStateHandle): AlbumViewModel
  }
}
