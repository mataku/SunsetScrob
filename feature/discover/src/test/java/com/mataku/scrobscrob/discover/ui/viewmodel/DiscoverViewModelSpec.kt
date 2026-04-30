package com.mataku.scrobscrob.discover.ui.viewmodel

import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.ChartTopArtists
import com.mataku.scrobscrob.core.entity.ChartTopTracks
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.ChartTrackArtist
import com.mataku.scrobscrob.core.entity.LovedTrack
import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.data.repository.ChartRepository
import com.mataku.scrobscrob.data.repository.UserRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class DiscoverViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  val lovedTracks = listOf(
    LovedTrack(
      artist = "aespa",
      images = persistentListOf(),
      name = "Drama",
      url = "https://www.last.fm/music/aespa/_/Drama",
    ),
  )
  val topTracks = ChartTopTracks(
    topTracks = persistentListOf(
      ChartTrack(
        name = "My Love Mine All Mine",
        playCount = "16319592",
        listeners = "843008",
        url = "https://www.last.fm/music/Mitski/_/My+Love+Mine+All+Mine",
        artist = ChartTrackArtist(name = "Mitski", url = "https://www.last.fm/music/Mitski"),
        imageList = persistentListOf(),
        mbid = "",
      ),
    ),
    pagingAttr = PagingAttr(),
  )
  val topArtists = ChartTopArtists(
    topArtists = persistentListOf(
      ChartArtist(
        name = "The Weeknd",
        playCount = "578258095",
        listeners = "3680672",
        url = "https://www.last.fm/music/The+Weeknd",
        imageList = persistentListOf(),
      ),
    ),
    pagingAttr = PagingAttr(),
  )

  describe("init") {
    it("combines loved tracks, top tracks, and top artists into the UI state") {
      val chartRepository = mockk<ChartRepository>()
      val userRepository = mockk<UserRepository>()
      coEvery { userRepository.getLovedTracks(1) } returns flowOf(lovedTracks)
      coEvery { chartRepository.topTracks(1) } returns flowOf(topTracks)
      coEvery { chartRepository.topArtists(1) } returns flowOf(topArtists)

      val viewModel = DiscoverViewModel(chartRepository, userRepository)

      viewModel.uiState.value.let { state ->
        state.recentlyLovedTracks.size shouldBe 1
        state.recentlyLovedTracks[0].name shouldBe "Drama"
        state.topTracks.size shouldBe 1
        state.topTracks[0].name shouldBe "My Love Mine All Mine"
        state.topArtists.size shouldBe 1
        state.topArtists[0].name shouldBe "The Weeknd"
      }
    }

    it("substitutes empty lists when individual flows fail") {
      val chartRepository = mockk<ChartRepository>()
      val userRepository = mockk<UserRepository>()
      coEvery { userRepository.getLovedTracks(1) } returns flow { throw RuntimeException("boom") }
      coEvery { chartRepository.topTracks(1) } returns flow { throw RuntimeException("boom") }
      coEvery { chartRepository.topArtists(1) } returns flowOf(topArtists)

      val viewModel = DiscoverViewModel(chartRepository, userRepository)

      viewModel.uiState.value.let { state ->
        state.recentlyLovedTracks.size shouldBe 0
        state.topTracks.size shouldBe 0
        state.topArtists.size shouldBe 1
      }
    }
  }
})
