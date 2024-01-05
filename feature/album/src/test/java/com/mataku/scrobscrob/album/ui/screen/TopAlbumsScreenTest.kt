package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.data.repository.AlbumRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
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

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
  qualifiers = RobolectricDeviceQualifiers.Pixel7,
  sdk = [33]
)
class TopAlbumsScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val albumRepository = mockk<AlbumRepository>()
  private val usernameRepository = mockk<UsernameRepository>()
  private val navController = mockk<NavController>()
  private val username = "sunsetscrob"
  private val timeRangeFiltering = TimeRangeFiltering.OVERALL

  private val topAlbums = (1..20).map {
    TopAlbumInfo(
      artist = "SooooooooooooooooooooooLongArtistname ${it}",
      title = "SooooooooooooooooooooooLongAlbumname ${it}",
      imageList = persistentListOf(),
      playCount = "100${it}",
      url = ""
    )
  }

  private val topAlbumsPage2 = (21..40).map {
    TopAlbumInfo(
      artist = "SooooooooooooooooooooooLongArtistname ${it}",
      title = "SooooooooooooooooooooooLongAlbumname ${it}",
      imageList = persistentListOf(),
      playCount = "100${it}",
      url = ""
    )
  }

  @Before
  fun setup() {
    every {
      usernameRepository.username()
    }.returns(username)

    coEvery {
      albumRepository.fetchTopAlbums(
        page = 1,
        username = username,
        timeRangeFiltering = TimeRangeFiltering.OVERALL
      )
    }.returns(flowOf(topAlbums))

    coEvery {
      albumRepository.fetchTopAlbums(
        page = 2,
        username = username,
        timeRangeFiltering = TimeRangeFiltering.OVERALL
      )
    }.returns(flowOf(topAlbumsPage2))

    coEvery {
      albumRepository.fetchTopAlbums(
        page = 3,
        username = username,
        timeRangeFiltering = TimeRangeFiltering.OVERALL
      )
    }.returns(flowOf(emptyList()))
  }

  @Test
  fun layout() {
    composeRule.setContent {
      SunsetThemePreview {
        TopAlbumsScreen(
          viewModel = TopAlbumsViewModel(
            topAlbumsRepository = albumRepository,
            usernameRepository = usernameRepository
          ),
          navController = navController
        )
      }
    }
    composeRule.onNode(isRoot()).captureRoboImage(
      filePath = "screenshot/top_albums_screen.png",
    )
  }
}
