package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.album.ui.viewmodel.AlbumViewModel
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.test_helper.integration.fixture.sampleAlbumInfo
import com.mataku.scrobscrob.ui_common.component.designsystem.rememberSunsetTopAppBarScrollBehavior
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class TopAlbumsScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

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
    composeRule.captureScreenshot(
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
    composeRule.captureScreenshot(
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
    composeRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.PixelTablet,
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
    composeRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.PixelTablet,
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
        composeRule.onAllNodesWithText("SooooooooooooooooooooooLongAlbumname 1").onFirst().performClick()
        composeRule.waitForIdle()
      },
      fileName = "top_albums_screen_tablet_two_pane.png"
    )
  }
}
