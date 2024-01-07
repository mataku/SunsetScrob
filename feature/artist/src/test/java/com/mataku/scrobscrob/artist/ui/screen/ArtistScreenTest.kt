package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.material3.Surface
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.Stats
import com.mataku.scrobscrob.core.entity.Tag
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
}
