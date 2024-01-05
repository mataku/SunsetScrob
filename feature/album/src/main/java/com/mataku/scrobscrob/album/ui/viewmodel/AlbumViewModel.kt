package com.mataku.scrobscrob.album.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.data.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
  private val albumRepository: AlbumRepository
) : ViewModel() {

  data class AlbumUiState(
    val isLoading: Boolean,
    val topAlbumInfo: TopAlbumInfo
  )
}
