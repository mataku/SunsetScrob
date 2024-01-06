package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.album.ui.molecule.AlbumMetaData
import com.mataku.scrobscrob.album.ui.molecule.AlbumTrackList
import com.mataku.scrobscrob.album.ui.viewmodel.AlbumViewModel
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.AlbumInfoTrack
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.ui_common.molecule.SimpleWiki
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.molecule.TopTags
import com.mataku.scrobscrob.ui_common.molecule.WikiCell
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.persistentListOf
import java.util.Date

@Composable
fun AlbumScreen(
  viewModel: AlbumViewModel,
  onAlbumLoadMoreTap: (String) -> Unit
) {
  val uiState by viewModel.uiState.collectAsState()

//  val systemUiController = rememberSystemUiController()
//  val currentTheme = LocalAppTheme.current
//  systemUiController.setNavigationBarColor(
//    color = currentTheme.backgroundColor()
//  )

  AlbumContent(
    artworkUrl = uiState.preloadArtworkUrl,
    albumName = uiState.preloadAlbumName,
    artistName = uiState.preloadArtistName,
    albumInfo = uiState.albumInfo,
    onAlbumLoadMoreTap = onAlbumLoadMoreTap
  )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AlbumContent(
  artworkUrl: String,
  albumName: String,
  artistName: String,
  albumInfo: AlbumInfo?,
  onAlbumLoadMoreTap: (String) -> Unit
) {
  val screenWidth = LocalConfiguration.current.screenWidthDp
  val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)

  BackdropScaffold(
    appBar = {},
    headerHeight = screenWidth.dp,
    backLayerContent = {
      val imageData = artworkUrl.ifBlank {
        com.mataku.scrobscrob.ui_common.R.drawable.no_image
      }
      Column {
        SunsetImage(
          imageData = imageData,
          contentDescription = "artwork image",
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1F)
        )
        SunsetImage(
          imageData = imageData,
          contentDescription = "artwork image",
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1F)
            .offset(
              y = (screenWidth / 2).dp
            )
        )
      }
    },
    frontLayerContent = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
      ) {
        AlbumMetaData(
          albumName = albumName,
          artistName = artistName,
          listeners = albumInfo?.listeners,
          playCount = albumInfo?.playCount,
          modifier = Modifier
            .padding(16.dp)
        )
        val tags = albumInfo?.tags
        if (tags?.isNotEmpty() == true) {
          TopTags(
            tagList = tags,
            modifier = Modifier
              .padding(
                vertical = 16.dp
              )
          )
        }

        Divider()

        albumInfo?.let { album ->
          Spacer(modifier = Modifier.height(16.dp))
          AlbumTrackList(
            tracks = album.tracks,
            modifier = Modifier
              .padding(
                horizontal = 16.dp
              )
          )
          Spacer(modifier = Modifier.height(16.dp))
          Divider()
          val wiki = album.wiki
          if (wiki == null) {
            SimpleWiki(
              name = albumName,
              url = album.url,
              onUrlTap = onAlbumLoadMoreTap,
              modifier = Modifier
                .padding(16.dp)
            )
          } else {
            WikiCell(
              wiki = wiki,
              name = albumName,
              modifier = Modifier
                .padding(
                  16.dp
                ),
              onUrlTap = onAlbumLoadMoreTap
            )
          }
        }
        Spacer(modifier = Modifier.height(32.dp))
      }
    },
    modifier = Modifier
      .fillMaxSize()
      .background(
        color = MaterialTheme.colorScheme.background
      ),
    scaffoldState = scaffoldState,
    frontLayerBackgroundColor = MaterialTheme.colorScheme.background,
    frontLayerScrimColor = Color.Transparent
  )
}

@Composable
@Preview(showBackground = true)
private fun AlbumContentPreview() {
  SunsetThemePreview {
    Surface {
      AlbumContent(
        artworkUrl = "",
        albumName = "Drama",
        artistName = "aespa",
        albumInfo = AlbumInfo(
          artistName = "aespa",
          albumName = "Drama",
          tracks = persistentListOf(
            AlbumInfoTrack(
              duration = "100",
              name = "Drama",
              url = ""
            ),
            AlbumInfoTrack(
              duration = "100",
              name = "Drama",
              url = ""
            ),
            AlbumInfoTrack(
              duration = "100",
              name = "Drama",
              url = ""
            )
          ),
          images = persistentListOf(),
          listeners = "10000",
          playCount = "1000",
          url = "",
          tags = persistentListOf(
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
            ),
            Tag(
              name = "K-POP",
              url = ""
            ),
          ),
          wiki = Wiki(
            published = Date(),
            content = "\"Clocks\" emerged in <b>conception during the late</b>stages into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. With this, they thought it was too late for the song's inclusion in the albumclude contrast, contradictions and urgency. Chris Martin sings of being in the state of \"helplessness ...",
            summary = "\"Clocks\" emerged in <b>conception during the late stages</b> into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. <a href=\"http://www.last.fm/music/Coldplay/_/Clocks\">Read more on Last.fm</a>.",
          )
        ),
        onAlbumLoadMoreTap = {}
      )
    }
  }
}
