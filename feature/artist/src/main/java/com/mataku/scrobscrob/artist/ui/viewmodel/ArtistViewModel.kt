package com.mataku.scrobscrob.artist.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.data.repository.ArtistRepository
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@AssistedInject
class ArtistViewModel(
  private val artistRepository: ArtistRepository,
  @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val artistName = savedStateHandle.get<String>("artistName")
  private val artworkUrl = savedStateHandle.get<String>("artworkUrl") ?: ""

  val uiState: StateFlow<ArtistUiState>
    field = MutableStateFlow(ArtistUiState())

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

  @Immutable
  data class ArtistUiState(
    val isLoading: Boolean = false,
    val artistInfo: ArtistInfo? = null,
    val preloadArtistName: String = "",
    val preloadArtworkUrl: String = ""
  )

  @AssistedFactory
  @ViewModelAssistedFactoryKey(ArtistViewModel::class)
  @ContributesIntoMap(AppScope::class)
  fun interface Factory : ViewModelAssistedFactory {
    override fun create(extras: CreationExtras): ArtistViewModel =
      create(extras.createSavedStateHandle())

    fun create(@Assisted savedStateHandle: SavedStateHandle): ArtistViewModel
  }
}
