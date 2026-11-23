package com.mataku.scrobscrob.scrobble.ui.viewmodel

import com.mataku.scrobscrob.core.entity.TrackArtist
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.data.repository.TrackRepository
import com.mataku.scrobscrob.scrobble.ui.navigation.TrackDetailKey
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class TrackViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  val trackRepository = mockk<TrackRepository>()
  val trackName = "Yesterday"
  val artistName = "The Beatles"
  val key = TrackDetailKey(
    trackName = trackName,
    artistName = artistName,
    imageUrl = "",
    id = "1",
  )
  val trackInfo = TrackInfo(
    artist = TrackArtist(name = artistName, url = ""),
    listeners = "100",
    playCount = "1000",
    url = "",
    name = trackName,
  )

  beforeEach {
    clearMocks(trackRepository)
  }

  describe("#init") {
    context("initializes with track name and artist name from TrackDetailKey") {
      it("should fetch track info using key fields") {
        coEvery {
          trackRepository.getInfo(
            trackName = trackName,
            artistName = artistName
          )
        }.returns(flowOf())

        val viewModel = TrackViewModel(
          trackRepository = trackRepository,
          key = key,
        )

        coVerify(exactly = 1) {
          trackRepository.getInfo(
            trackName = trackName,
            artistName = artistName
          )
        }

        viewModel.uiState.value.isLoading shouldBe false
      }
    }

    context("track info is fetched") {
      it("exposes it and stops loading") {
        coEvery { trackRepository.getInfo(trackName, artistName) } returns flowOf(trackInfo)

        val viewModel = TrackViewModel(trackRepository = trackRepository, key = key)

        viewModel.uiState.value shouldBe TrackViewModel.TrackUiState(
          isLoading = false,
          trackInfo = trackInfo,
          event = null,
        )
      }
    }

    context("fetching track info fails") {
      it("emits TrackInfoFetchFailure and stops loading") {
        coEvery { trackRepository.getInfo(trackName, artistName) } returns flow { error("offline") }

        val viewModel = TrackViewModel(trackRepository = trackRepository, key = key)

        viewModel.uiState.value shouldBe TrackViewModel.TrackUiState(
          isLoading = false,
          trackInfo = null,
          event = TrackViewModel.UiEvent.TrackInfoFetchFailure,
        )
      }
    }
  }

  describe("#loveOrUnloveTrack") {
    beforeEach {
      coEvery { trackRepository.getInfo(trackName, artistName) } returns flowOf(trackInfo)
    }

    context("the track is not loved yet") {
      it("loves it and marks it as loved") {
        coEvery { trackRepository.loveTrack(trackName, artistName) } returns flowOf(Unit)
        val viewModel = TrackViewModel(trackRepository = trackRepository, key = key)

        viewModel.loveOrUnloveTrack(trackInfo)

        coVerify(exactly = 1) { trackRepository.loveTrack(trackName, artistName) }
        coVerify(exactly = 0) { trackRepository.unloveTrack(any(), any()) }
        viewModel.uiState.value.trackInfo shouldBe trackInfo.copy(userLoved = true)
      }
    }

    context("the track is already loved") {
      it("unloves it and marks it as not loved") {
        val lovedTrack = trackInfo.copy(userLoved = true)
        coEvery { trackRepository.unloveTrack(trackName, artistName) } returns flowOf(Unit)
        val viewModel = TrackViewModel(trackRepository = trackRepository, key = key)

        viewModel.loveOrUnloveTrack(lovedTrack)

        coVerify(exactly = 1) { trackRepository.unloveTrack(trackName, artistName) }
        coVerify(exactly = 0) { trackRepository.loveTrack(any(), any()) }
        viewModel.uiState.value.trackInfo shouldBe trackInfo.copy(userLoved = false)
      }
    }

    context("the request fails") {
      it("keeps the current state and accepts the next request") {
        coEvery { trackRepository.loveTrack(trackName, artistName) } returns flow { error("offline") }
        val viewModel = TrackViewModel(trackRepository = trackRepository, key = key)

        viewModel.loveOrUnloveTrack(trackInfo)
        viewModel.uiState.value.trackInfo shouldBe trackInfo

        coEvery { trackRepository.loveTrack(trackName, artistName) } returns flowOf(Unit)
        viewModel.loveOrUnloveTrack(trackInfo)
        viewModel.uiState.value.trackInfo shouldBe trackInfo.copy(userLoved = true)
      }
    }

    context("a request is still in flight") {
      it("ignores further taps") {
        val gate = CompletableDeferred<Unit>()
        coEvery { trackRepository.loveTrack(trackName, artistName) } returns flow {
          gate.await()
          emit(Unit)
        }
        val viewModel = TrackViewModel(trackRepository = trackRepository, key = key)

        viewModel.loveOrUnloveTrack(trackInfo)
        viewModel.loveOrUnloveTrack(trackInfo)
        gate.complete(Unit)

        coVerify(exactly = 1) { trackRepository.loveTrack(trackName, artistName) }
      }
    }
  }
})
