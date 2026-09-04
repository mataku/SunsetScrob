package com.mataku.scrobscrob.discover.screen

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
import com.mataku.scrobscrob.test_helper.integration.ScreenshotDevice
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class DiscoverScreenTest {
  private val chartRepository = mockk<ChartRepository>()
  private val userRepository = mockk<UserRepository>()

  private val topArtists = (1..20).map {
    ChartArtist(
      name = "sooooo looooong artists $it",
      playCount = "10000$it",
      listeners = "100$it",
      imageList = persistentListOf(),
      url = ""
    )
  }

  private val topTracks = (1..20).map {
    ChartTrack(
      name = "sooooo looooong track $it",
      playCount = "10000$it",
      listeners = "100$it",
      imageList = persistentListOf(),
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
      images = persistentListOf()
    )
  }

  @BeforeEach
  fun setup() {
    coEvery {
      chartRepository.topTracks(1)
    }.returns(
      flowOf(
        ChartTopTracks(
          topTracks = topTracks.toImmutableList(),
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
      chartRepository.topArtists(1)
    }.returns(
      flowOf(
        ChartTopArtists(
          topArtists = topArtists.toImmutableList(),
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
    }.returns(flowOf(recentLovedTracks))
  }

  @Test
  fun layout() {
    val viewModel = DiscoverViewModel(chartRepository, userRepository)
    captureScreenshot(
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
    captureScreenshot(
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

  @Test
  fun layout_tablet() {
    val viewModel = DiscoverViewModel(chartRepository, userRepository)
    captureScreenshot(
      device = ScreenshotDevice.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        DiscoverScreen(
          viewModel = viewModel,
          navigateToWebView = mockk()
        )
      },
      fileName = "discover_screen_tablet.png"
    )
  }
}
