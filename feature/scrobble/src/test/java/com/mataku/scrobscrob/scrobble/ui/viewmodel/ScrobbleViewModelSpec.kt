package com.mataku.scrobscrob.scrobble.ui.viewmodel

import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.RecentTracks
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

private fun recentTrack(name: String, date: String? = null): RecentTrack = RecentTrack(
  artistName = "aespa",
  images = persistentListOf(),
  albumName = "Drama - The 4th Mini Album",
  name = name,
  url = "https://www.last.fm/music/aespa/_/$name",
  date = date,
)

class ScrobbleViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("init / fetchRecentTracks") {
    it("populates recentTracks and advances pagination when results come back") {
      val repository = mockk<ScrobbleRepository>()
      val tracks = listOf(recentTrack("Drama", date = "30 Dec 2023, 15:50"))
      coEvery { repository.recentTracks(page = 1) } returns flowOf(
        RecentTracks(
          tracks = tracks.toImmutableList(),
          pagingAttr = PagingAttr(totalPages = "10"),
        ),
      )

      val viewModel = ScrobbleViewModel(repository)

      viewModel.uiState.value.let { state ->
        state.isLoading shouldBe false
        state.recentTracks.size shouldBe 1
        state.recentTracks[0].name shouldBe "Drama"
        state.hasNext shouldBe true
      }
    }

    it("flags hasNext=false when totalPages returns no further data") {
      val repository = mockk<ScrobbleRepository>()
      coEvery { repository.recentTracks(page = 1) } returns flowOf(
        RecentTracks(
          tracks = persistentListOf(recentTrack("Drama")),
          pagingAttr = PagingAttr(totalPages = "1"),
        ),
      )

      val viewModel = ScrobbleViewModel(repository)
      viewModel.uiState.value.hasNext shouldBe false
    }

    it("emits a Scrobble Error event when fetching fails") {
      val repository = mockk<ScrobbleRepository>()
      coEvery { repository.recentTracks(page = 1) } returns flow {
        throw RuntimeException("boom")
      }

      val viewModel = ScrobbleViewModel(repository)
      viewModel.uiState.value.let { state ->
        state.uiEvents.size shouldBe 1
        state.uiEvents[0].shouldBeInstanceOf<ScrobbleViewModel.ScrobbleUiEvent.Error>()
        state.hasNext shouldBe false
      }
    }
  }

  describe("#refresh") {
    it("replaces recentTracks when the first track changes") {
      val repository = mockk<ScrobbleRepository>()
      val initial = listOf(recentTrack("Drama", date = "30 Dec 2023, 15:50"))
      val refreshed = listOf(recentTrack("Trick or Trick", date = "31 Dec 2023, 11:00"))
      coEvery { repository.recentTracks(page = 1) } returnsMany listOf(
        flowOf(RecentTracks(initial.toImmutableList(), PagingAttr(totalPages = "1"))),
        flowOf(RecentTracks(refreshed.toImmutableList(), PagingAttr(totalPages = "1"))),
      )

      val viewModel = ScrobbleViewModel(repository)
      viewModel.refresh()

      viewModel.uiState.value.recentTracks[0].name shouldBe "Trick or Trick"
      viewModel.uiState.value.isRefreshing shouldBe false
    }

    it("does nothing when already loading or refreshing") {
      val repository = mockk<ScrobbleRepository>()
      coEvery { repository.recentTracks(page = 1) } returns flow {
        // Block forever so the VM stays loading.
        kotlinx.coroutines.awaitCancellation()
      }

      val viewModel = ScrobbleViewModel(repository)
      // The init call leaves hasNext=true, isLoading still true (suspended).
      viewModel.refresh()

      // The repository should have been called exactly once (from init).
      coVerify(exactly = 1) { repository.recentTracks(page = 1) }
    }
  }

  describe("initial state") {
    it("starts with no events and an empty track list before any data emits") {
      val repository = mockk<ScrobbleRepository>()
      coEvery { repository.recentTracks(page = 1) } returns flowOf(
        RecentTracks(persistentListOf(), PagingAttr()),
      )

      val viewModel = ScrobbleViewModel(repository)
      viewModel.uiState.value.uiEvents.shouldBeEmpty()
    }
  }
})
