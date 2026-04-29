package com.mataku.scrobscrob.artist.ui.viewmodel

import com.mataku.scrobscrob.artist.ui.navigation.ArtistKey
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.Stats
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf

class ArtistViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  val artistRepository = mockk<ArtistRepository>()
  val artistName = "aespa"
  val artworkUrl = "https://mataku.com/sample.png"
  val artistInfo = ArtistInfo(
    name = artistName,
    url = "",
    images = persistentListOf(),
    tags = persistentListOf(),
    stats = Stats(
      listeners = "1000000",
      playCount = "10000000"
    ),
    wiki = null
  )

  describe("ArtistViewModel") {
    it("initializes with values from ArtistKey") {
      val repo = mockk<ArtistRepository>(relaxed = true)
      val key = ArtistKey(artistName = "Radiohead", artworkUrl = "", contentId = "1")
      val viewModel = ArtistViewModel(repo, key)
      viewModel.uiState.value.preloadArtistName shouldBe "Radiohead"
    }

    context("artistName is empty") {
      it("should return initial state") {
        val key = ArtistKey(artistName = "", artworkUrl = artworkUrl, contentId = "1")
        val viewModel = ArtistViewModel(
          artistRepository = artistRepository,
          key = key
        )
        viewModel.uiState.value shouldBe ArtistViewModel.ArtistUiState()

        coVerify(exactly = 0) {
          artistRepository.artistInfo(any())
        }
      }
    }

    context("required params are passed") {
      it("should return fetched ArtistInfo") {
        val key = ArtistKey(artistName = artistName, artworkUrl = artworkUrl, contentId = "1")
        coEvery {
          artistRepository.artistInfo(artistName)
        }.returns(flowOf(artistInfo))

        val viewModel = ArtistViewModel(
          artistRepository = artistRepository,
          key = key
        )
        viewModel.uiState.value shouldBe ArtistViewModel.ArtistUiState(
          isLoading = false,
          artistInfo = artistInfo,
          preloadArtistName = artistName,
          preloadArtworkUrl = artworkUrl
        )

        coVerify(exactly = 1) {
          artistRepository.artistInfo(artistName)
        }
      }
    }
  }
})
