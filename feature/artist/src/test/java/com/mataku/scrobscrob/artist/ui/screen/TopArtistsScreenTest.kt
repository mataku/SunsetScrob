package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.core.entity.TopArtists
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.rememberSunsetTopAppBarScrollBehavior
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
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

  private val animatedContentScope = mockk<AnimatedContentScope>(relaxed = true)

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
    }.returns(
      flowOf(
        TopArtists(
          artists = artistInfoList.toImmutableList(),
          pagingAttr = PagingAttr(
            page = "1",
            perPage = "20"
          )
        )
      )
    )

    coEvery {
      artistRepository.fetchTopArtists(
        page = 2,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }.returns(
      flowOf(
        TopArtists(
          artists = artistInfoListPage2.toImmutableList(),
          pagingAttr = PagingAttr(
            page = "1",
            perPage = "20"
          )
        )
      )
    )

    coEvery {
      artistRepository.fetchTopArtists(
        page = 3,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }.returns(
      flowOf(
        TopArtists(
          artists = persistentListOf(),
          pagingAttr = PagingAttr(
            page = "1",
            perPage = "20"
          )
        )
      )
    )
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
    val viewModel = TopArtistsViewModel(
      topArtistsRepository = artistRepository,
      usernameRepository = usernameRepository
    )
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
    val viewModel = TopArtistsViewModel(
      topArtistsRepository = artistRepository,
      usernameRepository = usernameRepository
    )
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
}
