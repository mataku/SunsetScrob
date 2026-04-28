package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.RecentTracks
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel
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
class ScrobbleScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val scrobbleRepository = mockk<ScrobbleRepository>()

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

  private val recentScrobbleTracksPage2 = (21..40).map {
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

  private fun List<RecentTrack>.asRecentTracks(totalPages: String): RecentTracks =
    RecentTracks(
      tracks = toImmutableList(),
      pagingAttr = PagingAttr(totalPages = totalPages)
    )

  @Before
  fun setup() {
    coEvery {
      scrobbleRepository.recentTracks(1)
    }.returns(flowOf(recentScrobbleTracks.asRecentTracks(totalPages = "3")))

    coEvery {
      scrobbleRepository.recentTracks(2)
    }.returns(flowOf(recentScrobbleTracksPage2.asRecentTracks(totalPages = "3")))

    coEvery {
      scrobbleRepository.recentTracks(3)
    }.returns(flowOf(emptyList<RecentTrack>().asRecentTracks(totalPages = "3")))

    every {
      sharedTransitionScope.isTransitionActive
    }.returns(false)
  }

  @Test
  fun layout() {
    val viewModel = ScrobbleViewModel(scrobbleRepository)
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        ScrobbleScreen(
          viewModel = viewModel,
          navigateToTrackDetail = { _, _ -> },
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
    val viewModel = ScrobbleViewModel(scrobbleRepository)
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        ScrobbleScreen(
          viewModel = viewModel,
          navigateToTrackDetail = { _, _ -> },
          topAppBarScrollBehavior = rememberSunsetTopAppBarScrollBehavior(),
          sharedTransitionScope = mockk(),
          animatedContentScope = mockk()
        )
      },
      fileName = "scrobble_screen_light.png"
    )
  }
}
