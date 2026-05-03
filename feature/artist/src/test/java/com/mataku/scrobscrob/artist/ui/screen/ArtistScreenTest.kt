package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.artist.ui.navigation.ArtistKey
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.test_helper.integration.fixture.sampleArtistInfo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class ArtistScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val artistName = "aespa"
  private val artworkUrl = ""
  private val artistRepository = mockk<ArtistRepository>()

  private val animatedContentScope = mockk<AnimatedContentScope>(relaxed = true)

  @Before
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
    composeRule.captureScreenshot(
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
    composeRule.captureScreenshot(
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
    composeRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.PixelTablet,
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
