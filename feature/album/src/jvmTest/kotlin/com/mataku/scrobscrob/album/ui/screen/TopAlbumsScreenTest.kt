package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import com.mataku.scrobscrob.album.ui.viewmodel.AlbumViewModel
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.test_helper.integration.ScreenshotDevice
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.test_helper.integration.fixture.sampleAlbumInfo
import com.mataku.scrobscrob.ui_common.component.designsystem.rememberSunsetTopAppBarScrollBehavior
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class TopAlbumsScreenTest {
  private val topAlbums = (1..20).map {
    TopAlbumInfo(
      artist = "SooooooooooooooooooooooLongArtistname $it",
      title = "SooooooooooooooooooooooLongAlbumname $it",
      imageList = persistentListOf(),
      playCount = "100$it",
      url = ""
    )
  }

  private val animatedContentScope = mockk<AnimatedContentScope>(relaxed = true)

  private fun stubTopAlbumsViewModel(): TopAlbumsViewModel =
    mockk<TopAlbumsViewModel>(relaxed = true).apply {
      every { uiState } returns MutableStateFlow(
        TopAlbumsViewModel.TopAlbumsUiState(
          isLoading = false,
          topAlbums = topAlbums.toImmutableList(),
          hasNext = false,
          timeRangeFiltering = TimeRangeFiltering.OVERALL
        )
      )
    }

  @Test
  fun layout() {
    val viewModel = stubTopAlbumsViewModel()
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          TopAlbumsScreen(
            viewModel = viewModel,
            navigateToAlbumInfo = { _, _ -> },
            topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
            sharedTransitionScope = this,
            animatedContentScope = animatedContentScope,
            albumViewModelProvider = { mockk(relaxed = true) },
            navigateToWebView = {}
          )
        }
      },
      fileName = "top_albums_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = stubTopAlbumsViewModel()
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        SharedTransitionLayout {
          TopAlbumsScreen(
            viewModel = viewModel,
            navigateToAlbumInfo = { _, _ -> },
            topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
            sharedTransitionScope = this,
            animatedContentScope = animatedContentScope,
            albumViewModelProvider = { mockk(relaxed = true) },
            navigateToWebView = {}
          )
        }
      },
      fileName = "top_albums_screen_light.png"
    )
  }

  @Test
  fun layout_tablet() {
    val viewModel = stubTopAlbumsViewModel()
    captureScreenshot(
      device = ScreenshotDevice.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          TopAlbumsScreen(
            viewModel = viewModel,
            navigateToAlbumInfo = { _, _ -> },
            topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
            sharedTransitionScope = this,
            animatedContentScope = animatedContentScope,
            albumViewModelProvider = { mockk(relaxed = true) },
            navigateToWebView = {}
          )
        }
      },
      fileName = "top_albums_screen_tablet.png"
    )
  }

  @Test
  fun layout_tablet_two_pane() {
    val topAlbumsViewModel = stubTopAlbumsViewModel()
    val albumViewModel = mockk<AlbumViewModel>(relaxed = true).apply {
      every { uiState } returns MutableStateFlow(
        AlbumViewModel.AlbumUiState(
          isLoading = false,
          albumInfo = sampleAlbumInfo,
          preloadArtistName = sampleAlbumInfo.artistName,
          preloadAlbumName = sampleAlbumInfo.albumName,
          preloadArtworkUrl = ""
        )
      )
    }
    captureScreenshot(
      device = ScreenshotDevice.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          TopAlbumsScreen(
            viewModel = topAlbumsViewModel,
            navigateToAlbumInfo = { _, _ -> },
            topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
            sharedTransitionScope = this,
            animatedContentScope = animatedContentScope,
            albumViewModelProvider = { albumViewModel },
            navigateToWebView = {}
          )
        }
      },
      actionsBeforeCapturing = {
        onAllNodesWithText("SooooooooooooooooooooooLongAlbumname 1").onFirst().performClick()
        waitForIdle()
      },
      fileName = "top_albums_screen_tablet_two_pane.png"
    )
  }
}
