package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.TrackRepository
import com.mataku.scrobscrob.scrobble.ui.navigation.TrackDetailKey
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.test_helper.integration.fixture.sampleTrackInfo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class TrackScreenTest {
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

  @BeforeEach
  fun setup() {
    coEvery {
      trackRepository.getInfo(
        trackName = trackName,
        artistName = artistName
      )
    }.returns(flowOf(sampleTrackInfo))
  }

  @Test
  fun layout() {
    val viewModel = TrackViewModel(
      trackRepository = trackRepository,
      key = key,
    )

    captureScreenshot(
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
  fun layout_light() {
    val viewModel = TrackViewModel(
      trackRepository = trackRepository,
      key = key,
    )

    captureScreenshot(
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
