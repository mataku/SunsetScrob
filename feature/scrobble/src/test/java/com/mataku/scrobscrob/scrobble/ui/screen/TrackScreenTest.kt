package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.material3.Surface
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.TrackArtist
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.core.entity.TrackWiki
import com.mataku.scrobscrob.data.repository.TrackRepository
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import io.mockk.coEvery
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
class TrackScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val artistName = "aespa"
  private val trackName = "Drama"
  private val artworkUrl: String? = null

  private val trackInfo = TrackInfo(
    artist = TrackArtist(
      name = "aespaaespaaespaaespaaespaaespaaespa",
      url = ""
    ),
    listeners = "100000",
    url = "https://example.com",
    name = "Drama",
    album = TrackAlbumInfo(
      artist = "aespaaespaaespaaespaaespaaespa",
      imageList = persistentListOf(),
      title = "Drama"
    ),
    playCount = "10000",
    topTags = persistentListOf(
      Tag(
        name = "K-POP",
        url = ""
      ),
      Tag(
        name = "K-POP",
        url = ""
      ),
      Tag(
        name = "K-POP",
        url = ""
      )
    ),
    wiki = TrackWiki(
      published = Date(),
      content = "\"Clocks\" emerged in <b>conception during the late</b>stages into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. With this, they thought it was too late for the song's inclusion in the albumclude contrast, contradictions and urgency. Chris Martin sings of being in the state of \"helplessness ...",
      summary = "\"Clocks\" emerged in <b>conception during the late stages</b> into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. <a href=\"http://www.last.fm/music/Coldplay/_/Clocks\">Read more on Last.fm</a>.",
    )
  )

  private val trackRepository = mockk<TrackRepository>()
  private val savedStateHandle = mockk<SavedStateHandle>()

  @Before
  fun setup() {
    every {
      savedStateHandle.get<String>("artistName")
    }.returns(artistName)

    every {
      savedStateHandle.get<String>("trackName")
    }.returns(trackName)
  }

  @Test
  fun layout() {
    coEvery {
      trackRepository.getInfo(
        trackName = trackName,
        artistName = artistName
      )
    }.returns(flowOf(trackInfo))

    val viewModel = TrackViewModel(
      trackRepository = trackRepository,
      savedStateHandle = savedStateHandle
    )

    composeRule.setContent {
      SunsetThemePreview {
        Surface {
          TrackScreen(
            trackName = trackName,
            artistName = artistName,
            artworkUrl = artworkUrl,
            trackViewModel = viewModel,
            navController = mockk()
          )
        }
      }
    }
    composeRule.onNode(isRoot()).captureRoboImage(
      filePath = "screenshot/track_screen.png",
    )
  }
}