package com.mataku.scrobscrob.album.ui.molecule

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.R as uiCommonR

@Composable
fun TopAlbum(
  album: AlbumInfo,
  onAlbumTap: () -> Unit,
  modifier: Modifier
) {

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .fillMaxWidth()
      .padding(8.dp)
      .clickable {
        onAlbumTap()
      },
  ) {
    val imageList = album.imageList
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
      contentDescription = album.title,
      size = 1000,
      contentScale = ContentScale.FillWidth,
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1F)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      album.title,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      style = SunsetTextStyle.body.copy(
        fontWeight = FontWeight.Bold
      )
    )
    Text(
      album.artist,
      maxLines = 1,
      style = SunsetTextStyle.label
    )
    Spacer(modifier = Modifier.height(2.dp))
    val playCountResource = if (album.playCount == "1") {
      uiCommonR.string.playcount
    } else {
      uiCommonR.string.playcounts
    }
    Text(
      stringResource(playCountResource, album.playCount),
      style = SunsetTextStyle.caption,
      color = MaterialTheme.colorScheme.onSecondary,
      maxLines = 1
    )
  }
}

@Preview(showBackground = true)
@Composable
fun TopAlbumPreview() {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(8.dp)
  ) {
    Image(
      painter = painterResource(uiCommonR.drawable.no_image),
      contentDescription = null,
      modifier = Modifier
        .size(128.dp)
    )
    Text(
      "生まれてから初めて見た夢",
      fontSize = 16.sp,
      modifier = Modifier
        .wrapContentSize()
    )
    Text(
      "乃木坂46",
      fontSize = 14.sp,
      modifier = Modifier
        .wrapContentSize()
    )
  }
}
