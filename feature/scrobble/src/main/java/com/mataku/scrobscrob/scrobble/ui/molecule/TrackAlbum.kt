package com.mataku.scrobscrob.scrobble.ui.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TrackAlbum(
  album: TrackAlbumInfo
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {

    Text(
      text = stringResource(id = R.string.label_track_album),
      style = SunsetTextStyle.headline
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ) {
      val imageUrl = album.imageList.imageUrl()
      val height = 80.dp

      val imageData = if (imageUrl == null || imageUrl.isBlank()) {
        com.mataku.scrobscrob.ui_common.R.drawable.no_image
      } else {
        imageUrl
      }

      SunsetImage(
        imageData = imageData,
        contentDescription = "Album artwork",
        modifier = Modifier
          .size(height)
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(
        modifier = Modifier
          .wrapContentWidth()
          .height(height),
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = album.title,
          modifier = Modifier.wrapContentSize(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = SunsetTextStyle.body.copy(
            fontWeight = FontWeight.Bold
          )
        )

        Spacer(modifier = Modifier.size(4.dp))

        Text(
          text = album.artist,
          modifier = Modifier.wrapContentSize(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = SunsetTextStyle.label
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun TrackAlbumPreview() {
  SunsetThemePreview() {
    Surface {
      TrackAlbum(
        album = TrackAlbumInfo(
          artist = "Perfume",
          title = "セラミックガール",
          imageList = persistentListOf(),
        )
      )
    }
  }
}
