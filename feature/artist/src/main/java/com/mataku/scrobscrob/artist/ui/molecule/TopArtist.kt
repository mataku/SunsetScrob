package com.mataku.scrobscrob.artist.ui.molecule

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mataku.scrobscrob.core.api.endpoint.Artist
import com.mataku.scrobscrob.core.api.endpoint.imageUrl
import com.mataku.scrobscrob.ui_common.R
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.R as uiCommonR

@Composable
fun TopArtist(artist: Artist, imageSize: Dp, onArtistTap: () -> Unit, modifier: Modifier) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .padding(8.dp)
      .clickable {
        onArtistTap()
      }
  ) {
    val imageList = artist.imageList
    val url = if (imageList == null || imageList.isEmpty()) {
      ""
    } else {
      imageList.imageUrl()
    }
    val painter = if (url == null || url.isBlank()) {
      painterResource(uiCommonR.drawable.no_image)
    } else {
      rememberAsyncImagePainter(model = url)
    }
    Image(
      painter = painter,
      contentDescription = artist.name,
      modifier = Modifier.size(imageSize)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      artist.name,
      fontSize = 16.sp,
      modifier = Modifier
        .wrapContentSize(),
      color = MaterialTheme.colors.onSurface,
      fontWeight = FontWeight.Bold,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    val playCountResource = if (artist.playcount == "1") {
      R.string.playcount
    } else {
      R.string.playcounts
    }
    Text(
      stringResource(playCountResource, artist.playcount),
      style = SunsetTextStyle.caption,
      maxLines = 1
    )
  }
}
