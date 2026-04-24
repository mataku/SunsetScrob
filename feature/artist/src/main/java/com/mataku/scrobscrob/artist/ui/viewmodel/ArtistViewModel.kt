package com.mataku.scrobscrob.artist.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.data.repository.ArtistRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class ArtistViewModel(
  private val artistRepository: ArtistRepository,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val artistName = savedStateHandle.get<String>("artistName")
  private val artworkUrl = savedStateHandle.get<String>("artworkUrl") ?: ""

  var uiState = MutableStateFlow(ArtistUiState())

  init {
    if (!artistName.isNullOrEmpty()) {
      uiState.update {
        it.copy(
          preloadArtistName = artistName,
          preloadArtworkUrl = artworkUrl
        )
      }
      fetchArtistDetail(artistName)
    }
  }

  private fun fetchArtistDetail(artistName: String) {
    artistRepository.artistInfo(artistName)
      .catch {
      }
      .onEach { artistInfo ->
        uiState.update { state ->
          state.copy(
            artistInfo = artistInfo
          )
        }
      }
      .launchIn(viewModelScope)
  }

  data class ArtistUiState(
    val isLoading: Boolean = false,
    val artistInfo: ArtistInfo? = null,
    val preloadArtistName: String = "",
    val preloadArtworkUrl: String = ""
  )
}
