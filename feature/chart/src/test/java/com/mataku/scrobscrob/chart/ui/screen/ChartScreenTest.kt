package com.mataku.scrobscrob.chart.ui.screen

import androidx.compose.material3.Surface
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import com.mataku.scrobscrob.chart.ui.viewmodel.ChartViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.ChartTopArtists
import com.mataku.scrobscrob.core.entity.ChartTopTracks
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.ChartTrackArtist
import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.data.repository.ChartRepository
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
  qualifiers = RobolectricDeviceQualifiers.Pixel7,
  sdk = [33]
)
class ChartScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private val chartRepository = mockk<ChartRepository>()

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

  @Before
  fun setup() {
    every {
      chartRepository.topArtists(1)
    }.returns(
      flowOf(
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

    every {
      chartRepository.topTracks(1)
    }.returns(
      flowOf(
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
  }

  @Test
  fun layout_artist() {
    val viewModel = ChartViewModel(chartRepository)
    composeTestRule.setContent {
      SunsetThemePreview {
        Surface {
          ChartScreen(
            viewModel = viewModel,
            navController = mockk()
          )
        }
      }
    }
    composeTestRule.onNode(isRoot()).captureRoboImage(
      filePath = "screenshot/chart_screen_artist.png",
    )
  }

  @Test
  fun layout_artist_light() {
    val viewModel = ChartViewModel(chartRepository)
    composeTestRule.setContent {
      SunsetThemePreview(theme = AppTheme.LIGHT) {
        Surface {
          ChartScreen(
            viewModel = viewModel,
            navController = mockk()
          )
        }
      }
    }
    composeTestRule.onNode(isRoot()).captureRoboImage(
      filePath = "screenshot/chart_screen_artist_light.png",
    )
  }

  @Test
  fun layout_track() {
    val viewModel = ChartViewModel(chartRepository)
    composeTestRule.setContent {
      SunsetThemePreview {
        Surface {
          ChartScreen(
            viewModel = viewModel,
            navController = mockk()
          )
        }
      }
    }
    composeTestRule.onNodeWithText("Track").performClick()

    composeTestRule.onRoot().captureRoboImage(
      filePath = "screenshot/chart_screen_track.png",
    )
  }

  @Test
  fun layout_track_light() {
    val viewModel = ChartViewModel(chartRepository)
    composeTestRule.setContent {
      SunsetThemePreview(theme = AppTheme.LIGHT) {
        Surface {
          ChartScreen(
            viewModel = viewModel,
            navController = mockk()
          )
        }
      }
    }
    composeTestRule.onNodeWithText("Track").performClick()

    composeTestRule.onRoot().captureRoboImage(
      filePath = "screenshot/chart_screen_track_light.png",
    )
  }
}
