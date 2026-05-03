package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.test_helper.integration.fixture.sampleArtistInfo
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
class TopArtistsScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val artistInfoList = (1..20).map {
    TopArtistInfo(
      name = "SoooooooooooooooLoooooooooooooooongName ${it}",
      imageList = persistentListOf(),
      topTags = persistentListOf(),
      playCount = "100${it}",
      url = ""
    )
  }

  private val animatedContentScope = mockk<AnimatedContentScope>(relaxed = true)

  private fun stubTopArtistsViewModel(): TopArtistsViewModel =
    mockk<TopArtistsViewModel>(relaxed = true).apply {
      every { uiState } returns MutableStateFlow(
        TopArtistsViewModel.TopArtistsUiState(
          isLoading = false,
          topArtists = artistInfoList.toImmutableList(),
          hasNext = false,
          selectedTimeRangeFiltering = TimeRangeFiltering.OVERALL,
        )
      )
    }

  @Test
  fun layout() {
    val viewModel = stubTopArtistsViewModel()
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          TopArtistsScreen(
            viewModel = viewModel,
            onArtistTap = { _, _ -> },
            topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
            animatedContentScope = animatedContentScope,
            sharedTransitionScope = this,
            artistViewModelProvider = { mockk(relaxed = true) },
            navigateToWebView = {}
          )
        }
      },
      fileName = "top_artists_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = stubTopArtistsViewModel()
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        SharedTransitionLayout {
          TopArtistsScreen(
            viewModel = viewModel,
            onArtistTap = { _, _ -> },
            topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
            animatedContentScope = animatedContentScope,
            sharedTransitionScope = this,
            artistViewModelProvider = { mockk(relaxed = true) },
            navigateToWebView = {}
          )
        }
      },
      fileName = "top_artists_screen_light.png"
    )
  }

  @Test
  fun layout_tablet() {
    val viewModel = stubTopArtistsViewModel()
    composeRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          TopArtistsScreen(
            viewModel = viewModel,
            onArtistTap = { _, _ -> },
            topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
            animatedContentScope = animatedContentScope,
            sharedTransitionScope = this,
            artistViewModelProvider = { mockk(relaxed = true) },
            navigateToWebView = {}
          )
        }
      },
      fileName = "top_artists_screen_tablet.png"
    )
  }

  @Test
  fun layout_tablet_two_pane() {
    val topArtistsViewModel = stubTopArtistsViewModel()
    val artistViewModel = mockk<ArtistViewModel>(relaxed = true).apply {
      every { uiState } returns MutableStateFlow(
        ArtistViewModel.ArtistUiState(
          isLoading = false,
          artistInfo = sampleArtistInfo,
          preloadArtistName = sampleArtistInfo.name,
          preloadArtworkUrl = ""
        )
      )
    }
    composeRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.PixelTablet,
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          TopArtistsScreen(
            viewModel = topArtistsViewModel,
            onArtistTap = { _, _ -> },
            topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
            animatedContentScope = animatedContentScope,
            sharedTransitionScope = this,
            artistViewModelProvider = { artistViewModel },
            navigateToWebView = {}
          )
        }
      },
      actionsBeforeCapturing = {
        composeRule.onAllNodesWithText("SoooooooooooooooLoooooooooooooooongName 1").onFirst().performClick()
        composeRule.waitForIdle()
      },
      fileName = "top_artists_screen_tablet_two_pane.png"
    )
  }
}
