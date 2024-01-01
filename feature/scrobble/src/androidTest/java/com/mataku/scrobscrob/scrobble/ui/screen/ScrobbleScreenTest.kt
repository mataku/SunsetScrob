package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScrobbleScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val scrobbleRepository = mockk<ScrobbleRepository>()

  private val recentScrobbleTracks = (1..20).map {
    RecentTrack(
      artistName = "SooooooooooooooooooLoooooooooooongArtistName ${it}",
      images = persistentListOf(),
      albumName = "SoooooooooooooooooooLoooooooooooongAlbumName ${it}",
      name = "track name ${it}",
      url = "",
      date = null
    )
  }

  private val recentScrobbleTracksPage2 = (21..40).map {
    RecentTrack(
      artistName = "SooooooooooooooooooLoooooooooooongArtistName ${it}",
      images = persistentListOf(),
      albumName = "SoooooooooooooooooooLoooooooooooongAlbumName ${it}",
      name = "track name ${it}",
      url = "",
      date = null
    )
  }

  @Before
  fun setup() {
    coEvery {
      scrobbleRepository.recentTracks(1)
    }.returns(flowOf(recentScrobbleTracks))

    coEvery {
      scrobbleRepository.recentTracks(2)
    }.returns(flowOf(recentScrobbleTracksPage2))

    coEvery {
      scrobbleRepository.recentTracks(3)
    }.returns(flowOf(emptyList()))
  }

  @Test
  fun scroll() = runTest {
    val viewModel = ScrobbleViewModel(scrobbleRepository)
    composeRule.setContent {
      SunsetTheme {
        Surface {
          ScrobbleScreen(
            viewModel = viewModel,
            navController = mockk()
          )
        }
      }
    }
    composeRule.awaitIdle()
    composeRule.onNodeWithTag("scrobble_list").performScrollToIndex(20)
    composeRule.awaitIdle()
    composeRule.onNodeWithTag("scrobble_list").performScrollToIndex(40)

    coVerify(exactly = 1) {
      scrobbleRepository.recentTracks(1)
    }
    coVerify(exactly = 1) {
      scrobbleRepository.recentTracks(2)
    }
    coVerify(exactly = 1) {
      scrobbleRepository.recentTracks(3)
    }
    // TODO: add screenshot tests
  }
}
