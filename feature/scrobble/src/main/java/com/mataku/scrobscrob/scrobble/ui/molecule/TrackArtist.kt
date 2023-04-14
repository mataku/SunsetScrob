package com.mataku.scrobscrob.scrobble.ui.molecule

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.persistentListOf

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

    val imageData = if (imageUrl == null || imageUrl.isBlank()) {
      com.mataku.scrobscrob.ui_common.R.drawable.no_image
    } else {
      imageUrl
    }

    SunsetImage(
      imageData = imageData,
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
    Surface {
      TrackArtist(
        ArtistInfo(
          name = "Nayeon",
          imageList = persistentListOf(),
          topTags = persistentListOf(),
          playCount = "1",
          url = ""
        )
      )
    }
  }
}
