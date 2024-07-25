package com.mataku.scrobscrob.scrobble.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.core.entity.presentation.toReadableIntValue
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SimpleWiki
import com.mataku.scrobscrob.ui_common.molecule.TopTags
import com.mataku.scrobscrob.ui_common.molecule.ValueDescription
import com.mataku.scrobscrob.ui_common.molecule.WikiCell
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun TrackDetail(
  trackInfo: TrackInfo,
  modifier: Modifier = Modifier,
  onUrlTap: (String) -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        horizontal = 16.dp
      )
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      TrackDescription(
        trackName = trackInfo.name,
        artistName = trackInfo.artist.name,
        modifier = Modifier.weight(1F)
      )

//      val userPlayCount = trackInfo.userPlayCount
//      if (userPlayCount > 0) {
//        Spacer(modifier = Modifier.width(8.dp))
//        ValueDescription(
//          value = userPlayCount.toReadableIntValue(),
//          label = "Scrobbles",
//          modifier = Modifier
//        )
//      }
//      Spacer(modifier = Modifier.width(20.dp))

      if (trackInfo.userLoved) {
        Image(
          painter = rememberVectorPainter(image = Icons.Default.Favorite),
          contentDescription = "favorite",
          colorFilter = ColorFilter.tint(
            color = Colors.Heart
          ),
          modifier = Modifier
            .size(30.dp)
        )
      }
    }

    TrackMetaData(
      listeners = trackInfo.listeners,
      playCount = trackInfo.playCount,
      userPlayCount = trackInfo.userPlayCount,
      modifier = Modifier
        .padding(
          top = 24.dp,
          bottom = 8.dp
        )
    )

    trackInfo.album?.let { album ->
      HorizontalDivider(
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            vertical = 16.dp
          ),
        color = MaterialTheme.colorScheme.onSecondary.copy(
          alpha = 0.7F
        )
      )
      TrackAlbum(album = album)
      if (trackInfo.topTags.isNotEmpty()) {
        TopTags(
          tagList = trackInfo.topTags,
          modifier = Modifier
            .padding(
              vertical = 16.dp
            )
            .layout { measurable, constraints ->
              val width = constraints.maxWidth + 32.dp.roundToPx()
              val placeable = measurable.measure(
                constraints.copy(
                  maxWidth = width
                )
              )
              layout(placeable.width, placeable.height) {
                placeable.place(0, 0)
              }
            }
            .fillMaxWidth()

        )
      } else {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
    HorizontalDivider()
    val wiki = trackInfo.wiki
    if (wiki == null) {
      SimpleWiki(
        name = trackInfo.name,
        url = trackInfo.url,
        onUrlTap = onUrlTap,
        modifier = Modifier
          .padding(
            top = 16.dp,
            bottom = 24.dp
          )
      )
    } else {
      WikiCell(
        wiki = wiki,
        name = trackInfo.name,
        modifier = Modifier
          .padding(
            vertical = 16.dp
          ),
        onUrlTap = onUrlTap
      )
    }
  }
}

@Composable
private fun TrackDescription(
  trackName: String,
  artistName: String,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    Text(
      text = trackName,
      style = SunsetTextStyle.body,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
      text = artistName,
      style = SunsetTextStyle.caption,
      modifier = Modifier,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Composable
private fun TrackMetaData(
  listeners: String?,
  playCount: String?,
  userPlayCount: String,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(40.dp)
  ) {
    listeners?.let {
      ValueDescription(
        value = it.toReadableIntValue(),
        label = "Listeners",
      )
    }
    playCount?.let {
      ValueDescription(
        value = it.toReadableIntValue(),
        label = "Plays"
      )
    }

    ValueDescription(
      value = userPlayCount.toReadableIntValue(),
      label = "You"
    )
  }
}

@Composable
@Preview(showBackground = true)
private fun TrackDetailPreview() {
  SunsetThemePreview {
    Surface {
      TrackDetail(
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
          wiki = Wiki(
            published = "01 January 2023",
            content = "\"Clocks\" emerged in <b>conception during the late</b>stages into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. With this, they thought it was too late for the song's inclusion in the albumclude contrast, contradictions and urgency. Chris Martin sings of being in the state of \"helplessness ...",
            summary = "\"Clocks\" emerged in <b>conception during the late stages</b> into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. <a href=\"http://www.last.fm/music/Coldplay/_/Clocks\">Read more on Last.fm</a>.",
          ),
          userPlayCount = "10000"
        ),
        modifier = Modifier
          .padding(
            vertical = 16.dp
          )
      ) {
      }
    }
  }
}

@Composable
@Preview(showBackground = true)
private fun TrackDetailUserLovedTrackPreview() {
  SunsetThemePreview {
    Surface {
      TrackDetail(
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
          wiki = Wiki(
            published = "01 January 2023",
            content = "\"Clocks\" emerged in <b>conception during the late</b>stages into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. With this, they thought it was too late for the song's inclusion in the albumclude contrast, contradictions and urgency. Chris Martin sings of being in the state of \"helplessness ...",
            summary = "\"Clocks\" emerged in <b>conception during the late stages</b> into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. <a href=\"http://www.last.fm/music/Coldplay/_/Clocks\">Read more on Last.fm</a>.",
          ),
          userPlayCount = "10000",
          userLoved = true
        ),
        modifier = Modifier
          .padding(
            vertical = 16.dp
          )
      ) {
      }
    }
  }
}
