package com.mataku.scrobscrob.artist.ui

import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.artist.ui.screen.TopArtistsScreen
import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
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
class TopArtistsScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val artistRepository = mockk<TopArtistsRepository>()
  private val usernameRepository = mockk<UsernameRepository>()
  private val navController = mockk<NavController>()
  private val username = "sunsetscrob"
  private val timeRangeFiltering = TimeRangeFiltering.OVERALL

  private val artistInfoList = (1..20).map {
    ArtistInfo(
      name = "SoooooooooooooooLoooooooooooooooongName ${it}",
      imageList = persistentListOf(),
      topTags = persistentListOf(),
      playCount = "100${it}",
      url = ""
    )
  }

  private val artistInfoListPage2 = (21..40).map {
    ArtistInfo(
      name = "SoooooooooooooooLoooooooooooooooongName ${it}",
      imageList = persistentListOf(),
      topTags = persistentListOf(),
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
      artistRepository.fetchTopArtists(
        page = 1,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }.returns(flowOf(artistInfoList))

    coEvery {
      artistRepository.fetchTopArtists(
        page = 2,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }.returns(flowOf(artistInfoListPage2))

    coEvery {
      artistRepository.fetchTopArtists(
        page = 3,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }.returns(flowOf(emptyList()))
  }

  @Test
  fun scroll() = runTest {
    val viewModel = TopArtistsViewModel(
      topArtistsRepository = artistRepository,
      usernameRepository = usernameRepository
    )
    composeRule.setContent {
      SunsetTheme {
        Surface {
          TopArtistsScreen(
            viewModel = viewModel,
            navController = navController
          )
        }
      }
    }
    composeRule.awaitIdle()
    composeRule.onNodeWithTag("artists_list").performScrollToIndex(19)
    composeRule.awaitIdle()
    composeRule.onNodeWithTag("artists_list").performScrollToIndex(39)

    coVerify(exactly = 1) {
      artistRepository.fetchTopArtists(
        page = 1,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }
    coVerify(exactly = 1) {
      artistRepository.fetchTopArtists(
        page = 2,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }
    coVerify(exactly = 1) {
      artistRepository.fetchTopArtists(
        page = 3,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }
    // TODO: add screenshot tests
  }
}
