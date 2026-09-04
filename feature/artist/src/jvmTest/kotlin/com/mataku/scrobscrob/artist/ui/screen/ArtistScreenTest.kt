package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import com.mataku.scrobscrob.artist.ui.navigation.ArtistKey
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.test_helper.integration.ScreenshotDevice
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.test_helper.integration.fixture.sampleArtistInfo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class ArtistScreenTest {
  private val artistName = "aespa"
  private val artworkUrl = ""
  private val artistRepository = mockk<ArtistRepository>()

  private val animatedContentScope = mockk<AnimatedContentScope>(relaxed = true)

  @BeforeEach
  fun setup() {
    every {
      artistRepository.artistInfo(artistName)
    }.returns(flowOf(sampleArtistInfo))
  }

  @Test
  fun layout() {
    val key = ArtistKey(artistName = artistName, artworkUrl = artworkUrl, contentId = "")
    val viewModel = ArtistViewModel(
      artistRepository = artistRepository,
      key = key
    )
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          ArtistScreen(
            viewModel = viewModel,
            onArtistLoadMoreTap = {},
            onBackPressed = {},
            animatedVisibilityScope = animatedContentScope,
            id = ""
          )
        }
      },
      fileName = "artist_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val key = ArtistKey(artistName = artistName, artworkUrl = artworkUrl, contentId = "")
    val viewModel = ArtistViewModel(
      artistRepository = artistRepository,
      key = key
    )
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        SharedTransitionLayout {
          ArtistScreen(
            viewModel = viewModel,
            onArtistLoadMoreTap = {},
            onBackPressed = {},
            id = "",
            animatedVisibilityScope = animatedContentScope
          )
        }
      },
      fileName = "artist_screen_light.png"
    )
  }

  @Test
  fun layout_tablet() {
    val key = ArtistKey(artistName = artistName, artworkUrl = artworkUrl, contentId = "")
    val viewModel = ArtistViewModel(
      artistRepository = artistRepository,
      key = key
    )
    captureScreenshot(
      device = ScreenshotDevice.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          ArtistScreen(
            viewModel = viewModel,
            onArtistLoadMoreTap = {},
            onBackPressed = {},
            animatedVisibilityScope = animatedContentScope,
            id = ""
          )
        }
      },
      fileName = "artist_screen_tablet.png"
    )
  }
}
