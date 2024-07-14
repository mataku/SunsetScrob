package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.album.ui.viewmodel.AlbumViewModel
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.AlbumInfoTrack
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.data.repository.AlbumRepository
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
class AlbumScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val artistName = "aespa"
  private val artworkUrl = ""
  private val albumName = "Drama"

  private val albumInfo = AlbumInfo(
    albumName = albumName,
    artistName = albumName,
    images = persistentListOf(),
    tags = persistentListOf(
      Tag("K-POP", ""),
      Tag("K-POP", ""),
      Tag("K-POP", ""),
      Tag("K-POP", ""),
    ),
    url = "",
    listeners = "1000000",
    playCount = "10000000",
    tracks = persistentListOf(
      AlbumInfoTrack(
        duration = "100",
        name = "Drama",
        url = ""
      ),
      AlbumInfoTrack(
        duration = "110",
        name = "Drama",
        url = ""
      ),
      AlbumInfoTrack(
        duration = null,
        name = "Drama",
        url = ""
      )
    )
  )

  private val albumRepository = mockk<AlbumRepository>()
  private val savedStateHandle = mockk<SavedStateHandle>()

  @Before
  fun setup() {
    every {
      savedStateHandle.get<String>("artistName")
    }.returns(artistName)

    every {
      savedStateHandle.get<String>("albumName")
    }.returns(albumName)

    every {
      savedStateHandle.get<String>("artworkUrl")
    }.returns(artworkUrl)

    every {
      albumRepository.albumInfo(
        albumName = albumName,
        artistName = artistName
      )
    }.returns(flowOf(albumInfo))
  }

  @Test
  fun layout() {
    val viewModel = AlbumViewModel(
      albumRepository = albumRepository,
      savedStateHandle = savedStateHandle
    )

    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        AlbumScreen(
          viewModel = viewModel,
          onAlbumLoadMoreTap = {},
          onBackPressed = {}
        )
      },
      fileName = "album_screen.png"
    )
  }

  @Test
  fun layout_light() {
    val viewModel = AlbumViewModel(
      albumRepository = albumRepository,
      savedStateHandle = savedStateHandle
    )

    composeRule.captureScreenshot(
      fileName = "album_screen_light.png",
      content = {
        AlbumScreen(
          viewModel = viewModel,
          onAlbumLoadMoreTap = {},
          onBackPressed = {}
        )
      },
      appTheme = AppTheme.LIGHT
    )
  }
}
