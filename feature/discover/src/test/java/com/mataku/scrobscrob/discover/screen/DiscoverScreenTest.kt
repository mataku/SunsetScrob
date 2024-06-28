package com.mataku.scrobscrob.discover.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.ChartTopArtists
import com.mataku.scrobscrob.core.entity.ChartTopTracks
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.ChartTrackArtist
import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.data.repository.ChartRepository
import com.mataku.scrobscrob.discover.ui.screen.DiscoverScreen
import com.mataku.scrobscrob.discover.ui.viewmodel.DiscoverViewModel
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
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
class DiscoverScreenTest {
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

    every {
      chartRepository.topTags(1)
    }.returns(
      flowOf(
        persistentListOf(
          Tag(
            name = "tag name",
            url = ""
          ),
          Tag(
            name = "tag name2",
            url = ""
          ),
          Tag(
            name = "tag name3",
            url = ""
          ),
          Tag(
            name = "tag name4",
            url = ""
          ),
        )
      )
    )
  }

  @Test
  fun layout() {
    val viewModel = DiscoverViewModel(chartRepository)
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        DiscoverScreen(
          viewModel = viewModel,
          navController = mockk()
        )
      },
      fileName = "discover_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = DiscoverViewModel(chartRepository)
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        DiscoverScreen(
          viewModel = viewModel,
          navController = mockk()
        )
      },
      fileName = "discover_screen_light.png"
    )
  }
}
