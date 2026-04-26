package com.mataku.scrobscrob.scrobble.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.component.SunsetText
import com.mataku.scrobscrob.ui_common.component.SunsetImage
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TrackArtist(
  artistInfo: TopArtistInfo,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {

    val height = 80.dp
    val imageUrl = artistInfo.imageList.imageUrl()

    SunsetImage(
      imageData = imageUrl,
      contentDescription = "Album artist artwork",
      modifier = Modifier
        .size(height)
    )

    Spacer(modifier = Modifier.width(16.dp))

    SunsetText.Body(
      text = artistInfo.name,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.wrapContentSize(),
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun TrackArtistPreview() {
  SunsetThemePreview {
    TrackArtist(
      TopArtistInfo(
        name = "Nayeon",
        imageList = persistentListOf(),
        topTags = persistentListOf(),
        playCount = "1",
        url = ""
      )
    )
  }
}
