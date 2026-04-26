package com.mataku.scrobscrob.album.ui.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AlbumInfoTrack
import com.mataku.scrobscrob.ui_common.component.SunsetText
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
      SunsetText.Headline(
        text = "Track list",
        maxLines = 1,
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
    SunsetText.Caption(
      text = index.toString(),
      modifier = Modifier.width(16.dp),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.width(8.dp))

    SunsetText.Body(
      text = trackName,
      fontWeight = FontWeight.Medium,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier
        .weight(1F)
    )

    Spacer(modifier = Modifier.width(32.dp))

    val durationRepresentation = duration.toReadableString()
    if (durationRepresentation != null) {
      SunsetText.Label(
        text = durationRepresentation,
      )
    }
  }
}

@Preview
@Composable
private fun AlbumTrackPreview() {
  SunsetThemePreview {
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
