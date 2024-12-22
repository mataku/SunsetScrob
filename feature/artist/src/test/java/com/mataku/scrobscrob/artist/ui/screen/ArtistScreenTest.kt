@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.Stats
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ArtistScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val artistName = "aespa"
  private val artworkUrl = ""
  private val artistRepository = mockk<ArtistRepository>()
  private val artistInfo = ArtistInfo(
    name = "Drama",
    url = "",
    images = persistentListOf(),
    tags = persistentListOf(
      Tag("K-POP", ""),
      Tag("K-POP", ""),
      Tag("K-POP", ""),
      Tag("K-POP", ""),
    ),
    stats = Stats(
      listeners = "1000000",
      playCount = "10000000"
    ),
    wiki = Wiki(
      published = "01 January 2023",
      summary = "aespa is a South Korean girl group formed by SM Entertainment. The group consists of four members: Karina (카리나), Giselle (지젤),  Winter (윈터) and Ningning (닝닝). They debuted on November 17, 2020 with the single \"Black Mamba\".\n\nThe group's name, aespa, combines the English initials of \"avatar\" and \"experience\" (Avatar X Experience) with the English word \"aspect\", meaning \"two sides\", to symbolize the idea of \"meeting another self and experiencing the new world\". <a href=\"https://www.last.fm/music/aespa\">Read more on Last.fm</a>",
      content = ""
    )
  )
  private val savedStateHandle = mockk<SavedStateHandle>()

  private val animatedContentScope = mockk<AnimatedContentScope>(relaxed = true)

  @Before
  fun setup() {
    every {
      savedStateHandle.get<String>("artistName")
    }.returns(artistName)

    every {
      savedStateHandle.get<String>("artworkUrl")
    }.returns(artworkUrl)

    every {
      artistRepository.artistInfo(artistName)
    }.returns(flowOf(artistInfo))
  }

  @Test
  fun layout() {
    val viewModel = ArtistViewModel(
      artistRepository = artistRepository,
      savedStateHandle = savedStateHandle
    )
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          ArtistScreen(
            viewModel = viewModel,
            onArtistLoadMoreTap = {},
            onBackPressed = {},
            animatedContentScope = animatedContentScope,
            id = ""
          )
        }
      },
      fileName = "artist_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = ArtistViewModel(
      artistRepository = artistRepository,
      savedStateHandle = savedStateHandle
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
            animatedContentScope = animatedContentScope
          )
        }
      },
      fileName = "artist_screen_light.png"
    )
  }
}
