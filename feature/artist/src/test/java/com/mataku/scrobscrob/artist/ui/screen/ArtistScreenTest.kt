package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.material3.Surface
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.Stats
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.util.Date

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
  qualifiers = RobolectricDeviceQualifiers.Pixel7,
  sdk = [33]
)
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
      published = Date(),
      summary = "aespa is a South Korean girl group formed by SM Entertainment. The group consists of four members: Karina (카리나), Giselle (지젤),  Winter (윈터) and Ningning (닝닝). They debuted on November 17, 2020 with the single \"Black Mamba\".\n\nThe group's name, aespa, combines the English initials of \"avatar\" and \"experience\" (Avatar X Experience) with the English word \"aspect\", meaning \"two sides\", to symbolize the idea of \"meeting another self and experiencing the new world\". <a href=\"https://www.last.fm/music/aespa\">Read more on Last.fm</a>",
      content = ""
    )
  )
  private val savedStateHandle = mockk<SavedStateHandle>()

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
    composeRule.setContent {
      SunsetThemePreview {
        Surface {
          ArtistScreen(
            viewModel = viewModel,
            onArtistLoadMoreTap = {}
          )
        }
      }
    }
    composeRule.onNode(isRoot()).captureRoboImage(
      filePath = "screenshot/artist_screen.png",
    )
  }

  @Test
  fun layout_light() {
    val viewModel = ArtistViewModel(
      artistRepository = artistRepository,
      savedStateHandle = savedStateHandle
    )
    composeRule.setContent {
      SunsetThemePreview(theme = AppTheme.LIGHT) {
        Surface {
          ArtistScreen(
            viewModel = viewModel,
            onArtistLoadMoreTap = {}
          )
        }
      }
    }
    composeRule.onNode(isRoot()).captureRoboImage(
      filePath = "screenshot/artist_screen_light.png",
    )
  }

}
