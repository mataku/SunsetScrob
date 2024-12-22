package com.mataku.scrobscrob.discover.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.ChartTopArtists
import com.mataku.scrobscrob.core.entity.ChartTopTracks
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.ChartTrackArtist
import com.mataku.scrobscrob.core.entity.LovedTrack
import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.data.repository.ChartRepository
import com.mataku.scrobscrob.data.repository.UserRepository
import com.mataku.scrobscrob.discover.ui.screen.DiscoverScreen
import com.mataku.scrobscrob.discover.ui.viewmodel.DiscoverViewModel
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class DiscoverScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private val chartRepository = mockk<ChartRepository>()
  private val userRepository = mockk<UserRepository>()

  private val topArtists = (1..20).map {
    ChartArtist(
      name = "sooooo looooong artists $it",
      playCount = "10000$it",
      listeners = "100$it",
      imageList = emptyList(),
      url = ""
    )
  }

  private val topTracks = (1..20).map {
    ChartTrack(
      name = "sooooo looooong track $it",
      playCount = "10000$it",
      listeners = "100$it",
      imageList = emptyList(),
      url = "",
      artist = ChartTrackArtist(
        name = "soooo looong artist $it",
        url = ""
      ),
      mbid = ""
    )
  }

  private val recentLovedTracks = (1..20).map {
    LovedTrack(
      name = "sooooo looooong loved track $it",
      artist = "soooo looong loved artist $it",
      url = "",
      images = emptyList()
    )
  }

  @Before
  fun setup() {
    coEvery {
      chartRepository.topTracksAsync(1)
    }.returns(
      Result.success(
        ChartTopTracks(
          topTracks = topTracks,
          pagingAttr = PagingAttr(
            page = "1",
            totalPages = "1",
            total = "1",
            perPage = "1"
          )
        )
      )
    )

    coEvery {
      chartRepository.topArtistsAsync(1)
    }.returns(
      Result.success(
        ChartTopArtists(
          topArtists = topArtists,
          pagingAttr = PagingAttr(
            page = "1",
            totalPages = "1",
            total = "1",
            perPage = "1"
          )
        )
      )
    )

    coEvery {
      userRepository.getLovedTracks(1)
    }.returns(Result.success(recentLovedTracks))
  }

  @Test
  fun layout() {
    val viewModel = DiscoverViewModel(chartRepository, userRepository)
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        DiscoverScreen(
          viewModel = viewModel,
          navigateToWebView = mockk()
        )
      },
      fileName = "discover_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = DiscoverViewModel(chartRepository, userRepository)
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        DiscoverScreen(
          viewModel = viewModel,
          navigateToWebView = mockk()
        )
      },
      fileName = "discover_screen_light.png"
    )
  }
}
