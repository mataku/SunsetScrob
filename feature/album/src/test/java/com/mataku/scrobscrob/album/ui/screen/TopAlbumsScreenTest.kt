@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.data.repository.AlbumRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class TopAlbumsScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val albumRepository = mockk<AlbumRepository>()
  private val usernameRepository = mockk<UsernameRepository>()
  private val username = "sunsetscrob"
  private val timeRangeFiltering = TimeRangeFiltering.OVERALL

  private val topAlbums = (1..20).map {
    TopAlbumInfo(
      artist = "SooooooooooooooooooooooLongArtistname $it",
      title = "SooooooooooooooooooooooLongAlbumname $it",
      imageList = persistentListOf(),
      playCount = "100$it",
      url = ""
    )
  }

  private val topAlbumsPage2 = (21..40).map {
    TopAlbumInfo(
      artist = "SooooooooooooooooooooooLongArtistname $it",
      title = "SooooooooooooooooooooooLongAlbumname $it",
      imageList = persistentListOf(),
      playCount = "100$it",
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
      albumRepository.fetchTopAlbums(
        page = 1,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }.returns(flowOf(topAlbums))

    coEvery {
      albumRepository.fetchTopAlbums(
        page = 2,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }.returns(flowOf(topAlbumsPage2))

    coEvery {
      albumRepository.fetchTopAlbums(
        page = 3,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      )
    }.returns(flowOf(emptyList()))
  }

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          TopAlbumsScreen(
            viewModel = TopAlbumsViewModel(
              topAlbumsRepository = albumRepository,
              usernameRepository = usernameRepository
            ),
            navigateToAlbumInfo = { _, _ -> },
            topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            sharedTransitionScope = this,
            animatedContentScope = animatedContentScope
          )
        }
      },
      fileName = "top_albums_screen.png"
    )
  }

  @Test
  fun layout_light() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        SharedTransitionLayout {
          TopAlbumsScreen(
            viewModel = TopAlbumsViewModel(
              topAlbumsRepository = albumRepository,
              usernameRepository = usernameRepository
            ),
            navigateToAlbumInfo = { _, _ -> },
            topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            sharedTransitionScope = this,
            animatedContentScope = animatedContentScope
          )
        }
      },
      fileName = "top_albums_screen_light.png"
    )
  }
}
