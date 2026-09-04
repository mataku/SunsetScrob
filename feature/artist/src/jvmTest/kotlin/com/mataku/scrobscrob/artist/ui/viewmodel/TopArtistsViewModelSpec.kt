package com.mataku.scrobscrob.artist.ui.viewmodel

import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.core.entity.TopArtists
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

private fun topArtist(name: String): TopArtistInfo = TopArtistInfo(
  name = name,
  imageList = persistentListOf(),
  topTags = persistentListOf(),
  playCount = "1",
  url = "https://www.last.fm/music/$name",
)

class TopArtistsViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("init") {
    context("username is blank") {
      it("skips the fetch and disables pagination") {
        val repository = mockk<TopArtistsRepository>()
        val usernameRepository = mockk<UsernameRepository>()
        every { usernameRepository.asyncUsername() } returns flowOf("")

        val viewModel = TopArtistsViewModel(repository, usernameRepository)

        viewModel.uiState.value.isLoading shouldBe false
        viewModel.uiState.value.hasNext shouldBe false
        coVerify(exactly = 0) { repository.fetchTopArtists(any(), any(), any()) }
      }
    }

    context("username is set") {
      it("fetches the first page and populates topArtists") {
        val repository = mockk<TopArtistsRepository>()
        val usernameRepository = mockk<UsernameRepository>()
        every { usernameRepository.asyncUsername() } returns flowOf("matakucom")
        coEvery {
          repository.fetchTopArtists(
            page = 1,
            username = "matakucom",
            timeRangeFiltering = TimeRangeFiltering.OVERALL,
          )
        } returns flowOf(
          TopArtists(
            artists = listOf(topArtist("aespa")).toImmutableList(),
            pagingAttr = PagingAttr(totalPages = "10"),
          ),
        )

        val viewModel = TopArtistsViewModel(repository, usernameRepository)

        viewModel.uiState.value.let { state ->
          state.isLoading shouldBe false
          state.topArtists.size shouldBe 1
          state.topArtists[0].name shouldBe "aespa"
          state.hasNext shouldBe true
        }
      }

      it("disables pagination when the fetch fails") {
        val repository = mockk<TopArtistsRepository>()
        val usernameRepository = mockk<UsernameRepository>()
        every { usernameRepository.asyncUsername() } returns flowOf("matakucom")
        coEvery {
          repository.fetchTopArtists(any(), any(), any())
        } returns flow { throw RuntimeException("boom") }

        val viewModel = TopArtistsViewModel(repository, usernameRepository)

        viewModel.uiState.value.hasNext shouldBe false
      }
    }
  }

  describe("#updateTimeRange") {
    it("changes the filter and re-fetches with the new range") {
      val repository = mockk<TopArtistsRepository>()
      val usernameRepository = mockk<UsernameRepository>()
      every { usernameRepository.asyncUsername() } returns flowOf("matakucom")
      coEvery {
        repository.fetchTopArtists(
          page = 1,
          username = "matakucom",
          timeRangeFiltering = TimeRangeFiltering.OVERALL,
        )
      } returns flowOf(
        TopArtists(
          artists = listOf(topArtist("aespa")).toImmutableList(),
          pagingAttr = PagingAttr(totalPages = "10"),
        ),
      )
      coEvery {
        repository.fetchTopArtists(
          page = 1,
          username = "matakucom",
          timeRangeFiltering = TimeRangeFiltering.LAST_30_DAYS,
        )
      } returns flowOf(
        TopArtists(
          artists = listOf(topArtist("PassCode")).toImmutableList(),
          pagingAttr = PagingAttr(totalPages = "10"),
        ),
      )

      val viewModel = TopArtistsViewModel(repository, usernameRepository)
      viewModel.updateTimeRange(TimeRangeFiltering.LAST_30_DAYS)

      viewModel.uiState.value.selectedTimeRangeFiltering shouldBe TimeRangeFiltering.LAST_30_DAYS
      viewModel.uiState.value.topArtists[0].name shouldBe "PassCode"
    }

    it("ignores the call when the filter is unchanged") {
      val repository = mockk<TopArtistsRepository>()
      val usernameRepository = mockk<UsernameRepository>()
      every { usernameRepository.asyncUsername() } returns flowOf("matakucom")
      coEvery {
        repository.fetchTopArtists(
          page = 1,
          username = "matakucom",
          timeRangeFiltering = TimeRangeFiltering.OVERALL,
        )
      } returns flowOf(
        TopArtists(
          artists = persistentListOf(),
          pagingAttr = PagingAttr(totalPages = "1"),
        ),
      )

      val viewModel = TopArtistsViewModel(repository, usernameRepository)
      viewModel.updateTimeRange(TimeRangeFiltering.OVERALL)

      coVerify(exactly = 1) {
        repository.fetchTopArtists(
          page = 1,
          username = "matakucom",
          timeRangeFiltering = TimeRangeFiltering.OVERALL,
        )
      }
    }
  }
})
