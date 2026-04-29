package com.mataku.scrobscrob.album.ui.viewmodel

import com.mataku.scrobscrob.album.ui.navigation.AlbumKey
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.data.repository.AlbumRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf

class AlbumViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  val albumRepository = mockk<AlbumRepository>()
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

  describe("AlbumViewModel") {
    it("initializes with values from AlbumKey") {
      val repo = mockk<AlbumRepository>(relaxed = true)
      val key = AlbumKey(albumName = "Help!", artistName = "The Beatles", artworkUrl = "", contentId = "1")
      val viewModel = AlbumViewModel(repo, key)
      viewModel.uiState.value.preloadAlbumName shouldBe "Help!"
      viewModel.uiState.value.preloadArtistName shouldBe "The Beatles"
    }

    context("artistName is empty") {
      it("should return initial state") {
        val key = AlbumKey(albumName = albumName, artistName = "", artworkUrl = artworkUrl, contentId = "1")
        val viewModel = AlbumViewModel(
          albumRepository = albumRepository,
          key = key
        )
        viewModel.uiState.value shouldBe AlbumViewModel.AlbumUiState()

        coVerify(exactly = 0) {
          albumRepository.albumInfo(any(), any())
        }
      }
    }

    context("albumName is empty") {
      it("should return initial state") {
        val key = AlbumKey(albumName = "", artistName = artistName, artworkUrl = artworkUrl, contentId = "1")
        val viewModel = AlbumViewModel(
          albumRepository = albumRepository,
          key = key
        )
        viewModel.uiState.value shouldBe AlbumViewModel.AlbumUiState()

        coVerify(exactly = 0) {
          albumRepository.albumInfo(any(), any())
        }
      }
    }

    context("required params are passed") {
      it("should return fetched AlbumInfo") {
        val key = AlbumKey(albumName = albumName, artistName = artistName, artworkUrl = artworkUrl, contentId = "1")
        coEvery {
          albumRepository.albumInfo(
            albumName = albumName,
            artistName = artistName
          )
        }.returns(flowOf(albumInfo))

        val viewModel = AlbumViewModel(
          albumRepository = albumRepository,
          key = key
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
