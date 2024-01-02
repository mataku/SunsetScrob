package com.mataku.scrobscrob.artist.ui.molecule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.R
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.R as uiCommonR

@Composable
fun TopArtist(
  artist: ArtistInfo,
  onArtistTap: () -> Unit,
  modifier: Modifier
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .padding(8.dp)
      .clickable {
        onArtistTap()
      }
  ) {
    val imageList = artist.imageList
    val url = if (imageList.isEmpty()) {
      ""
    } else {
      imageList.imageUrl()
    }
    val imageData = if (url == null || url.isBlank()) {
      uiCommonR.drawable.no_image
    } else {
      url
    }

    SunsetImage(
      imageData = imageData,
      contentDescription = artist.name,
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1F),
      contentScale = ContentScale.FillWidth
    )

    Spacer(modifier = Modifier.height(8.dp))
    Text(
      artist.name,
      modifier = Modifier,
      style = SunsetTextStyle.body,
      fontWeight = FontWeight.Bold,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    val playCountResource = if (artist.playCount == "1") {
      R.string.playcount
    } else {
      R.string.playcounts
    }
    Text(
      stringResource(playCountResource, artist.playCount),
      style = SunsetTextStyle.caption,
      color = MaterialTheme.colorScheme.onSecondary,
      maxLines = 1
    )
  }
}
