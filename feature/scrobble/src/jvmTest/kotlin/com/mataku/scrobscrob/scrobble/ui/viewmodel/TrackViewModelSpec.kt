package com.mataku.scrobscrob.scrobble.ui.viewmodel

import com.mataku.scrobscrob.data.repository.TrackRepository
import com.mataku.scrobscrob.scrobble.ui.navigation.TrackDetailKey
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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

        viewModel.state.value.isLoading shouldBe false
      }
    }
  }
})
