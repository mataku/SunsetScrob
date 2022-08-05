package com.mataku.scrobscrob.scrobble.ui.molecule

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mataku.scrobscrob.core.api.endpoint.ArtistInfo
import com.mataku.scrobscrob.core.api.endpoint.imageUrl
import com.mataku.scrobscrob.core.entity.TopTags
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun TrackArtist(
  artistInfo: ArtistInfo
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {

    val height = 80.dp
    val imageUrl = artistInfo.imageList.imageUrl()

    AsyncImage(
      model = imageUrl ?: R.drawable.no_image,
      contentDescription = "Album artist artwork",
      modifier = Modifier
        .size(height)
    )

    Spacer(modifier = Modifier.width(16.dp))

    Text(
      text = artistInfo.name,
      modifier = Modifier.wrapContentSize(),
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
      style = SunsetTextStyle.body1.copy(
        fontWeight = FontWeight.Bold
      )
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun TrackArtistPreview() {
  SunsetThemePreview() {
    androidx.compose.material.Surface() {
      TrackArtist(
        ArtistInfo(
          name = "Nayeon", imageList = emptyList(),
          topTags = TopTags(emptyList())
        )
      )
    }
  }
}
