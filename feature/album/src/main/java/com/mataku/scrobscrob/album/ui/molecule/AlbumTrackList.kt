package com.mataku.scrobscrob.album.ui.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AlbumInfoTrack
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.extension.toReadableString
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AlbumTrackList(
  tracks: ImmutableList<AlbumInfoTrack>,
  modifier: Modifier = Modifier
) {
  if (tracks.isNotEmpty()) {

    Column(
      modifier = modifier
    ) {
      Text(
        text = "Track list",
        style = SunsetTextStyle.headline,
        maxLines = 1,
        modifier = Modifier
      )
      Spacer(modifier = Modifier.height(8.dp))
      tracks.forEachIndexed { index, track ->
        AlbumTrack(
          trackName = track.name,
          duration = track.duration,
          index = index + 1
        )
      }
    }
  }
}

@Composable
private fun AlbumTrack(
  trackName: String,
  duration: String?,
  index: Int,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        vertical = 16.dp
      ),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = index.toString(),
      style = SunsetTextStyle.caption,
      modifier = Modifier.width(16.dp),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.width(8.dp))

    Text(
      text = trackName,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      style = SunsetTextStyle.body.copy(
        fontWeight = FontWeight.Medium
      ),
      modifier = Modifier
        .weight(1F)
    )

    Spacer(modifier = Modifier.width(32.dp))

    val durationRepresentation = duration.toReadableString()
    if (durationRepresentation != null) {
      Text(
        text = durationRepresentation,
        style = SunsetTextStyle.label
      )
    }
  }
}

@Preview
@Composable
private fun AlbumTrackPreview() {
  SunsetThemePreview {
    Surface(onClick = {}) {
      AlbumTrackList(
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
        modifier = Modifier.padding(
          horizontal = 16.dp
        )
      )
    }
  }
}
