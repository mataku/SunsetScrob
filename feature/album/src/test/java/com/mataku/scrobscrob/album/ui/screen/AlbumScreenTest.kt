package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.album.ui.navigation.AlbumKey
import com.mataku.scrobscrob.album.ui.viewmodel.AlbumViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.AlbumRepository
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.test_helper.integration.fixture.sampleAlbumInfo
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
class AlbumScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val artistName = "aespa"
  private val artworkUrl = ""
  private val albumName = "Drama"

  private val albumRepository = mockk<AlbumRepository>()
  private val animatedContentScope = mockk<AnimatedContentScope>(relaxed = true)

  @Before
  fun setup() {
    every {
      albumRepository.albumInfo(
        albumName = albumName,
        artistName = artistName
      )
    }.returns(flowOf(sampleAlbumInfo))
  }

  @Test
  fun layout() {
    val key = AlbumKey(albumName = albumName, artistName = artistName, artworkUrl = artworkUrl, contentId = "")
    val viewModel = AlbumViewModel(
      albumRepository = albumRepository,
      key = key
    )

    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          AlbumScreen(
            viewModel = viewModel,
            onAlbumLoadMoreTap = {},
            onBackPressed = {},
            animatedVisibilityScope = animatedContentScope,
            id = ""
          )
        }
      },
      fileName = "album_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val key = AlbumKey(albumName = albumName, artistName = artistName, artworkUrl = artworkUrl, contentId = "")
    val viewModel = AlbumViewModel(
      albumRepository = albumRepository,
      key = key
    )

    composeRule.captureScreenshot(
      fileName = "album_screen_light.png",
      content = {
        SharedTransitionLayout {
          AlbumScreen(
            viewModel = viewModel,
            onAlbumLoadMoreTap = {},
            onBackPressed = {},
            animatedVisibilityScope = animatedContentScope,
            id = ""
          )
        }
      },
      appTheme = AppTheme.LIGHT
    )
  }

  @Test
  fun layout_tablet() {
    val key = AlbumKey(albumName = albumName, artistName = artistName, artworkUrl = artworkUrl, contentId = "")
    val viewModel = AlbumViewModel(
      albumRepository = albumRepository,
      key = key
    )

    composeRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          AlbumScreen(
            viewModel = viewModel,
            onAlbumLoadMoreTap = {},
            onBackPressed = {},
            animatedVisibilityScope = animatedContentScope,
            id = ""
          )
        }
      },
      fileName = "album_screen_tablet.png"
    )
  }

}
