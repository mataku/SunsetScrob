package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.coEvery
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

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
  qualifiers = RobolectricDeviceQualifiers.Pixel7,
  sdk = [33]
)
class TopArtistsScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val artistRepository = mockk<TopArtistsRepository>()
  private val usernameRepository = mockk<UsernameRepository>()
  private val username = "sunsetscrob"
  private val timeRangeFiltering = TimeRangeFiltering.OVERALL

  private val artistInfoList = (1..20).map {
    TopArtistInfo(
      name = "SoooooooooooooooLoooooooooooooooongName ${it}",
      imageList = persistentListOf(),
      topTags = persistentListOf(),
      playCount = "100${it}",
      url = ""
    )
  }

  private val artistInfoListPage2 = (21..40).map {
    TopArtistInfo(
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
  fun layout() {
    val viewModel = TopArtistsViewModel(
      topArtistsRepository = artistRepository,
      usernameRepository = usernameRepository
    )
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        TopArtistsScreen(
          viewModel = viewModel,
          onArtistTap = {},
          topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        )
      },
      fileName = "top_artists_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = TopArtistsViewModel(
      topArtistsRepository = artistRepository,
      usernameRepository = usernameRepository
    )
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        TopArtistsScreen(
          viewModel = viewModel,
          onArtistTap = {},
          topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        )
      },
      fileName = "top_artists_screen_light.png"
    )
  }
}
