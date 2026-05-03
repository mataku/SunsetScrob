package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.TrackRepository
import com.mataku.scrobscrob.scrobble.ui.navigation.TrackDetailKey
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.test_helper.integration.fixture.sampleTrackInfo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class TrackScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val artistName = "aespa"
  private val trackName = "Drama"
  private val artworkUrl: String? = null

  private val trackRepository = mockk<TrackRepository>()
  private val key = TrackDetailKey(
    trackName = trackName,
    artistName = artistName,
    imageUrl = artworkUrl ?: "",
    id = "",
  )
  private val animatedContentScope = mockk<AnimatedContentScope>(relaxed = true)

  @Before
  fun setup() {
    coEvery {
      trackRepository.getInfo(
        trackName = trackName,
        artistName = artistName
      )
    }.returns(flowOf(sampleTrackInfo))
  }

  @Test
  fun layout() = runTest {
    val viewModel = TrackViewModel(
      trackRepository = trackRepository,
      key = key,
    )

    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          TrackScreen(
            trackName = trackName,
            artistName = artistName,
            artworkUrl = artworkUrl,
            trackViewModel = viewModel,
            navigateToWebView = mockk(),
            onBackPressed = mockk(),
            animatedVisibilityScope = animatedContentScope,
            id = ""
          )
        }
      },
      fileName = "track_screen.png"
    )
  }

  @Test
  fun layout_light() = runTest {
    val viewModel = TrackViewModel(
      trackRepository = trackRepository,
      key = key,
    )

    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        SharedTransitionLayout {
          TrackScreen(
            trackName = trackName,
            artistName = artistName,
            artworkUrl = artworkUrl,
            trackViewModel = viewModel,
            navigateToWebView = mockk(),
            onBackPressed = mockk(),
            animatedVisibilityScope = animatedContentScope,
            id = ""
          )
        }
      },
      fileName = "track_screen_light.png"
    )
  }

}
