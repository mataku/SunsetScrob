package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.test_helper.integration.fixture.sampleTrackInfo
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
class ScrobbleScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val recentScrobbleTracks = (1..20).map {
    RecentTrack(
      artistName = "SooooooooooooooooooLoooooooooooongArtistName $it",
      images = persistentListOf(),
      albumName = "SoooooooooooooooooooLoooooooooooongAlbumName $it",
      name = "track name $it",
      url = "",
      date = null
    )
  }

  private val sharedTransitionScope = mockk<SharedTransitionScope>(relaxed = true)

  private fun stubScrobbleViewModel(): ScrobbleViewModel =
    mockk<ScrobbleViewModel>(relaxed = true).apply {
      every { uiState } returns MutableStateFlow(
        ScrobbleViewModel.ScrobbleUiState(
          isLoading = false,
          isRefreshing = false,
          uiEvents = persistentListOf(),
          recentTracks = recentScrobbleTracks.toImmutableList(),
          hasNext = false,
        )
      )
    }

  @Test
  fun layout() {
    val viewModel = stubScrobbleViewModel()
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        ScrobbleScreen(
          viewModel = viewModel,
          navigateToTrackDetail = { _, _ -> },
          trackViewModelProvider = { mockk(relaxed = true) },
          navigateToWebView = {},
          topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
          animatedContentScope = mockk(),
          sharedTransitionScope = sharedTransitionScope
        )
      },
      fileName = "scrobble_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = stubScrobbleViewModel()
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        ScrobbleScreen(
          viewModel = viewModel,
          navigateToTrackDetail = { _, _ -> },
          trackViewModelProvider = { mockk(relaxed = true) },
          navigateToWebView = {},
          topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
          animatedContentScope = mockk(),
          sharedTransitionScope = mockk()
        )
      },
      fileName = "scrobble_screen_light.png"
    )
  }

  @Test
  fun layout_tablet() {
    val viewModel = stubScrobbleViewModel()
    composeRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        ScrobbleScreen(
          viewModel = viewModel,
          navigateToTrackDetail = { _, _ -> },
          trackViewModelProvider = { mockk(relaxed = true) },
          navigateToWebView = {},
          topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
          animatedContentScope = mockk(),
          sharedTransitionScope = sharedTransitionScope
        )
      },
      fileName = "scrobble_screen_tablet.png"
    )
  }

  @Test
  fun layout_tablet_two_pane() {
    val scrobbleViewModel = stubScrobbleViewModel()
    val trackViewModel = mockk<TrackViewModel>(relaxed = true).apply {
      every { state } returns MutableStateFlow(
        TrackViewModel.TrackUiState(
          isLoading = false,
          trackInfo = sampleTrackInfo,
          event = null,
        )
      )
    }
    composeRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          ScrobbleScreen(
            viewModel = scrobbleViewModel,
            navigateToTrackDetail = { _, _ -> },
            trackViewModelProvider = { trackViewModel },
            navigateToWebView = {},
            topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
            animatedContentScope = mockk(relaxed = true),
            sharedTransitionScope = this
          )
        }
      },
      actionsBeforeCapturing = {
        composeRule.onAllNodesWithText("track name 1").onFirst().performClick()
        composeRule.waitForIdle()
      },
      fileName = "scrobble_screen_tablet_two_pane.png"
    )
  }
}
