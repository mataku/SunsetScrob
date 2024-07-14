package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
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
  fun layout() {
    val viewModel = ScrobbleViewModel(scrobbleRepository)
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        ScrobbleScreen(
          viewModel = viewModel,
          navigateToTrackDetail = {},
          topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        )
      },
      fileName = "scrobble_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = ScrobbleViewModel(scrobbleRepository)
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        ScrobbleScreen(
          viewModel = viewModel,
          navigateToTrackDetail = {},
          topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        )
      },
      fileName = "scrobble_screen_light.png"
    )
  }
}
