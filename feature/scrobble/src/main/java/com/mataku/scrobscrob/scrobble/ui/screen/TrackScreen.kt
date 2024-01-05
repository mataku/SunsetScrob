package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.core.entity.TrackWiki
import com.mataku.scrobscrob.core.entity.presentation.toReadableIntValue
import com.mataku.scrobscrob.scrobble.ui.molecule.TrackAlbum
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SimpleWiki
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.molecule.TopTags
import com.mataku.scrobscrob.ui_common.molecule.ValueDescription
import com.mataku.scrobscrob.ui_common.molecule.Wiki
import com.mataku.scrobscrob.ui_common.navigateToWebView
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import kotlinx.collections.immutable.persistentListOf
import java.util.Date

@Composable
fun TrackScreen(
  trackName: String,
  artistName: String,
  artworkUrl: String?,
  trackViewModel: TrackViewModel,
  navController: NavController
) {
  val uiState by trackViewModel.state.collectAsState()

  val systemUiController = rememberSystemUiController()
  val currentTheme = LocalAppTheme.current
  systemUiController.setNavigationBarColor(
    color = currentTheme.backgroundColor()
  )

  TrackContent(
    artworkUrl = artworkUrl,
    trackInfo = uiState.trackInfo,
    onUrlTap = navController::navigateToWebView,
    artistName = artistName,
    trackName = trackName
  )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TrackContent(
  artworkUrl: String?,
  trackName: String,
  artistName: String,
  trackInfo: TrackInfo?,
  onUrlTap: (String) -> Unit
) {
  val screenWidth = LocalConfiguration.current.screenWidthDp
  val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
  BackdropScaffold(
    appBar = {},
    headerHeight = screenWidth.dp,
    backLayerContent = {
      val imageData = if (artworkUrl == null || artworkUrl.isBlank()) {
        com.mataku.scrobscrob.ui_common.R.drawable.no_image
      } else {
        artworkUrl
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
      ) {
        if (trackInfo == null) {
          TrackDetail2(
            trackName = trackName,
            artistName = artistName,
            modifier = Modifier
              .padding(
                16.dp
              ),
          )
        } else {
          TrackDetail(
            trackInfo = trackInfo,
            modifier = Modifier
              .padding(
                16.dp
              ),
          )
          Divider()
        }

        trackInfo?.let { track ->
          track.album?.let { album ->
            Spacer(modifier = Modifier.height(16.dp))
            TrackAlbum(album = album)
            if (track.topTags.isNotEmpty()) {
              TopTags(
                tagList = track.topTags,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(
                    16.dp
                  )
              )
            } else {
              Spacer(modifier = Modifier.height(16.dp))
            }
          }
          Divider()
          val wiki = track.wiki
          if (wiki == null) {
            SimpleWiki(
              name = trackName,
              url = track.url,
              onUrlTap = onUrlTap,
              modifier = Modifier
                .padding(
                  start = 16.dp,
                  end = 16.dp,
                  top = 16.dp,
                  bottom = 24.dp
                )
            )
          } else {
            Wiki(
              wiki = wiki,
              name = trackName,
              modifier = Modifier
                .padding(
                  16.dp
                )
            )
          }
        }
      }
    },
    modifier = Modifier
      .fillMaxSize()
      .background(
        color = Color.White
      ),
    scaffoldState = scaffoldState,
    frontLayerBackgroundColor = MaterialTheme.colorScheme.background,
    frontLayerContentColor = Color.Blue,
    frontLayerScrimColor = Color.Transparent
  )
}

@Composable
@Preview(showBackground = true)
private fun TrackContentPreview() {
  SunsetThemePreview {
    Surface {
      TrackContent(
        artworkUrl = null,
        trackInfo = TrackInfo(
          artist = com.mataku.scrobscrob.core.entity.TrackArtist(
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
        ),
        onUrlTap = {},
        artistName = "aespa",
        trackName = "Drama"
      )
    }
  }
}

@Composable
private fun TrackDetail2(
  trackName: String,
  artistName: String,
  modifier: Modifier = Modifier,
  listeners: String? = null,
  playCount: String? = null,
) {
  Row(
    modifier = modifier
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = Modifier
        .weight(1F)
    ) {
      Text(
        text = trackName,
        style = SunsetTextStyle.body,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = artistName,
        style = SunsetTextStyle.caption,
        modifier = Modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }

    Spacer(
      modifier = Modifier
        .width(32.dp)
    )

    TrackMetaData(
      listeners = listeners,
      playCount = playCount,
      modifier = Modifier
    )
  }
}

@Composable
private fun TrackDetail(
  trackInfo: TrackInfo,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = Modifier
        .weight(1F)
    ) {
      Text(
        text = trackInfo.name,
        style = SunsetTextStyle.body,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = trackInfo.artist.name,
        style = SunsetTextStyle.caption,
        modifier = Modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }

    Spacer(
      modifier = Modifier
        .width(32.dp)
    )

    TrackMetaData(
      trackInfo = trackInfo,
      modifier = Modifier
    )
  }
}

@Composable
private fun TrackMetaData(
  listeners: String?,
  playCount: String?,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(32.dp)
  ) {
    listeners?.let {
      ValueDescription(
        value = it.toReadableIntValue(),
        label = "Listeners"
      )
    }
    playCount?.let {
      ValueDescription(
        value = it.toReadableIntValue(),
        label = "Plays"
      )
    }
  }
}

@Composable
private fun TrackMetaData(
  trackInfo: TrackInfo,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(32.dp)
  ) {
    ValueDescription(
      value = trackInfo.listeners.toReadableIntValue(),
      label = "Listeners"
    )

    ValueDescription(
      value = trackInfo.playCount.toReadableIntValue(),
      label = "Plays"
    )
  }
}
