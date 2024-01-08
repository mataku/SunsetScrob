package com.mataku.scrobscrob.album.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.data.repository.AlbumRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf

class AlbumViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  val albumRepository = mockk<AlbumRepository>()
  val savedStateHandle = mockk<SavedStateHandle>()
  val albumName = "Drama"
  val artistName = "aespa"
  val artworkUrl = "https://mataku.com/sample.png"
  val albumInfo = AlbumInfo(
    albumName = albumName,
    artistName = artistName,
    images = persistentListOf(),
    listeners = "1000",
    playCount = "10000",
    url = "",
    tags = persistentListOf(),
    tracks = persistentListOf()
  )

  describe("#init") {
    beforeContainer {
      every {
        savedStateHandle.get<String>("artistName")
      }.returns(artistName)

      every {
        savedStateHandle.get<String>("albumName")
      }.returns(albumName)

      every {
        savedStateHandle.get<String>("artworkUrl")
      }.returns(artworkUrl)

      coEvery {
        albumRepository.albumInfo(
          albumName = albumName,
          artistName = artistName
        )
      }.returns(flowOf(albumInfo))
    }

    context("artistName is empty") {
      every {
        savedStateHandle.get<String>("artistName")
      }.returns("")

      it("should return initial state") {
        val viewModel = AlbumViewModel(
          albumRepository = albumRepository,
          savedStateHandle = savedStateHandle
        )
        viewModel.uiState.value shouldBe AlbumViewModel.AlbumUiState()

        coVerify(exactly = 0) {
          albumRepository.albumInfo(
            any(), any()
          )
        }
      }
    }

    context("albumName is empty") {
      every {
        savedStateHandle.get<String>("albumName")
      }.returns("")

      it("should return initial state") {
        val viewModel = AlbumViewModel(
          albumRepository = albumRepository,
          savedStateHandle = savedStateHandle
        )
        viewModel.uiState.value shouldBe AlbumViewModel.AlbumUiState()

        coVerify(exactly = 0) {
          albumRepository.albumInfo(
            any(), any()
          )
        }
      }
    }
    
    context("required params are passed") {
      it("should return fetched AlbumInfo") {
        val viewModel = AlbumViewModel(
          albumRepository = albumRepository,
          savedStateHandle = savedStateHandle
        )
        viewModel.uiState.value shouldBe AlbumViewModel.AlbumUiState(
          isLoading = false,
          albumInfo = albumInfo,
          preloadArtistName = artistName,
          preloadAlbumName = albumName,
          preloadArtworkUrl = artworkUrl
        )

        coVerify(exactly = 1) {
          albumRepository.albumInfo(
            albumName = albumName,
            artistName = artistName
          )
        }
      }
    }
  }
})
