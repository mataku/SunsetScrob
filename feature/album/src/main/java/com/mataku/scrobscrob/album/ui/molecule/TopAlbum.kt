package com.mataku.scrobscrob.album.ui.molecule

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.R as uiCommonR

@Composable
fun TopAlbum(album: Album, imageSize: Dp, onAlbumTap: () -> Unit, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
            .clickable {
                onAlbumTap()
            },
    ) {
        val imageList = album.imageList
        val url = if (imageList == null || imageList.isEmpty()) {
            ""
        } else {
            imageList.last().imageUrl
        }
        val painter = if (url.isBlank()) {
            painterResource(
                uiCommonR.drawable.no_image
            )
        } else {
            rememberImagePainter(url)
        }
        Image(
            painter = painter,
            contentDescription = album.name,
            modifier = Modifier.size(imageSize)
        )
        Text(
            album.name,
            fontSize = 16.sp,
            modifier = Modifier
                .wrapContentSize(),
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            album.artist.name,
            fontSize = 14.sp,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .wrapContentSize(),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(2.dp))
        val playCountResource = if (album.playcount == "1") {
            uiCommonR.string.playcount
        } else {
            uiCommonR.string.playcounts
        }
        Text(
            stringResource(playCountResource, album.playcount),
            style = SunsetTextStyle.caption,
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