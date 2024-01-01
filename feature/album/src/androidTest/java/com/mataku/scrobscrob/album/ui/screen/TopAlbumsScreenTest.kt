package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.data.repository.AlbumRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopAlbumsScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val albumRepository = mockk<AlbumRepository>()
  private val usernameRepository = mockk<UsernameRepository>()
  private val navController = mockk<NavController>()
  private val username = "sunsetscrob"
  private val timeRangeFiltering = TimeRangeFiltering.OVERALL

  private val topAlbums = (1..20).map {
    AlbumInfo(
      artist = "SooooooooooooooooooooooLongArtistname ${it}",
      title = "SooooooooooooooooooooooLongAlbumname ${it}",
      imageList = persistentListOf(),
      playCount = "100${it}",
      url = ""
    )
  }

  private val topAlbumsPage2 = (21..40).map {
    AlbumInfo(
      artist = "SooooooooooooooooooooooLongArtistname ${it}",
      title = "SooooooooooooooooooooooLongAlbumname ${it}",
      imageList = persistentListOf(),
      playCount = "100${it}",
      url = ""
    )
  }

  @Before
  fun setup() {
    every {
      usernameRepository.username()
    }.returns(username)

    coEvery {
      albumRepository.fetchTopAlbums(
        page = 1,
        username = username,
        timeRangeFiltering = TimeRangeFiltering.OVERALL
      )
    }.returns(flowOf(topAlbums))

    coEvery {
      albumRepository.fetchTopAlbums(
        page = 2,
        username = username,
        timeRangeFiltering = TimeRangeFiltering.OVERALL
      )
    }.returns(flowOf(topAlbumsPage2))

    coEvery {
      albumRepository.fetchTopAlbums(
        page = 3,
        username = username,
        timeRangeFiltering = TimeRangeFiltering.OVERALL
      )
    }.returns(flowOf(emptyList()))

  }

  @Test
  fun scroll() = runTest {
    composeRule.setContent {
      SunsetTheme {
        TopAlbumsScreen(
          viewModel = TopAlbumsViewModel(
            topAlbumsRepository = albumRepository,
            usernameRepository = usernameRepository
          ),
          navController = navController
        )
      }
    }
    composeRule.onNodeWithTag("album_list").performScrollToIndex(20)
    composeRule.awaitIdle()
    composeRule.onNodeWithTag("album_list").performScrollToIndex(40)
    composeRule.awaitIdle()
    coVerify(exactly = 1) {
      albumRepository.fetchTopAlbums(
        page = 1,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }
    coVerify(exactly = 1) {
      albumRepository.fetchTopAlbums(
        page = 2,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }
    coVerify(exactly = 1) {
      albumRepository.fetchTopAlbums(
        page = 3,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }
    // TODO: add screenshot tests
  }
}
