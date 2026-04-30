package com.mataku.scrobscrob.album.ui.viewmodel

import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.core.entity.TopAlbums
import com.mataku.scrobscrob.data.repository.AlbumRepository
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

private fun topAlbum(title: String): TopAlbumInfo = TopAlbumInfo(
  artist = "PassCode",
  title = title,
  imageList = persistentListOf(),
  playCount = "1",
  url = "https://www.last.fm/music/PassCode/$title",
)

class TopAlbumsViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("init") {
    context("username is blank") {
      it("skips the fetch and disables pagination") {
        val albumRepository = mockk<AlbumRepository>()
        val usernameRepository = mockk<UsernameRepository>()
        every { usernameRepository.username() } returns ""

        val viewModel = TopAlbumsViewModel(albumRepository, usernameRepository)

        viewModel.uiState.value.isLoading shouldBe false
        viewModel.uiState.value.hasNext shouldBe false
        coVerify(exactly = 0) { albumRepository.fetchTopAlbums(any(), any(), any()) }
      }
    }

    context("username is set") {
      it("fetches the first page and populates topAlbums") {
        val albumRepository = mockk<AlbumRepository>()
        val usernameRepository = mockk<UsernameRepository>()
        every { usernameRepository.username() } returns "matakucom"
        coEvery {
          albumRepository.fetchTopAlbums(
            page = 1,
            username = "matakucom",
            timeRangeFiltering = TimeRangeFiltering.OVERALL,
          )
        } returns flowOf(
          TopAlbums(
            albums = listOf(topAlbum("ZENITH")).toImmutableList(),
            pagingAttr = PagingAttr(totalPages = "10"),
          ),
        )

        val viewModel = TopAlbumsViewModel(albumRepository, usernameRepository)

        viewModel.uiState.value.let { state ->
          state.isLoading shouldBe false
          state.topAlbums.size shouldBe 1
          state.topAlbums[0].title shouldBe "ZENITH"
          state.hasNext shouldBe true
        }
      }

      it("disables pagination when the fetch fails") {
        val albumRepository = mockk<AlbumRepository>()
        val usernameRepository = mockk<UsernameRepository>()
        every { usernameRepository.username() } returns "matakucom"
        coEvery {
          albumRepository.fetchTopAlbums(any(), any(), any())
        } returns flow { throw RuntimeException("boom") }

        val viewModel = TopAlbumsViewModel(albumRepository, usernameRepository)

        viewModel.uiState.value.hasNext shouldBe false
      }
    }
  }

  describe("#updateTimeRange") {
    it("changes the filter and re-fetches with timeRangeFilteringChanged=true (resets pagination)") {
      val albumRepository = mockk<AlbumRepository>()
      val usernameRepository = mockk<UsernameRepository>()
      every { usernameRepository.username() } returns "matakucom"
      coEvery {
        albumRepository.fetchTopAlbums(
          page = 1,
          username = "matakucom",
          timeRangeFiltering = TimeRangeFiltering.OVERALL,
        )
      } returns flowOf(
        TopAlbums(
          albums = listOf(topAlbum("ZENITH")).toImmutableList(),
          pagingAttr = PagingAttr(totalPages = "10"),
        ),
      )
      coEvery {
        albumRepository.fetchTopAlbums(
          page = 1,
          username = "matakucom",
          timeRangeFiltering = TimeRangeFiltering.LAST_7_DAYS,
        )
      } returns flowOf(
        TopAlbums(
          albums = listOf(topAlbum("Drama")).toImmutableList(),
          pagingAttr = PagingAttr(totalPages = "10"),
        ),
      )

      val viewModel = TopAlbumsViewModel(albumRepository, usernameRepository)
      viewModel.updateTimeRange(TimeRangeFiltering.LAST_7_DAYS)

      viewModel.uiState.value.timeRangeFiltering shouldBe TimeRangeFiltering.LAST_7_DAYS
      viewModel.uiState.value.topAlbums[0].title shouldBe "Drama"
    }

    it("ignores the call when the filter is unchanged") {
      val albumRepository = mockk<AlbumRepository>()
      val usernameRepository = mockk<UsernameRepository>()
      every { usernameRepository.username() } returns "matakucom"
      coEvery {
        albumRepository.fetchTopAlbums(
          page = 1,
          username = "matakucom",
          timeRangeFiltering = TimeRangeFiltering.OVERALL,
        )
      } returns flowOf(
        TopAlbums(
          albums = persistentListOf(),
          pagingAttr = PagingAttr(totalPages = "1"),
        ),
      )

      val viewModel = TopAlbumsViewModel(albumRepository, usernameRepository)
      viewModel.updateTimeRange(TimeRangeFiltering.OVERALL)

      coVerify(exactly = 1) {
        albumRepository.fetchTopAlbums(
          page = 1,
          username = "matakucom",
          timeRangeFiltering = TimeRangeFiltering.OVERALL,
        )
      }
    }
  }
})
