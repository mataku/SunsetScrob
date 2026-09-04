package com.mataku.scrobscrob.artist.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.mataku.scrobscrob.artist.ui.navigation.ArtistKey
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.ui_common.navigation.requireKey
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
  @Assisted private val key: ArtistKey,
) : ViewModel() {

  val uiState: StateFlow<ArtistUiState>
    field = MutableStateFlow(ArtistUiState())

  init {
    if (key.artistName.isNotEmpty()) {
      uiState.update {
        it.copy(
          preloadArtistName = key.artistName,
          preloadArtworkUrl = key.artworkUrl
        )
      }
      fetchArtistDetail(key.artistName)
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
      create(extras.requireKey<ArtistKey>())

    fun create(@Assisted key: ArtistKey): ArtistViewModel
  }
}
