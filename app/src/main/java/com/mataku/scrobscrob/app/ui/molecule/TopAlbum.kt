package com.mataku.scrobscrob.app.ui.molecule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.core.api.endpoint.Album

@Composable
fun TopAlbum(album: Album, imageSize: Dp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        val url = album.imageList?.first()?.imageUrl
        val painter = if (url == null || url.isBlank()) {
            painterResource(R.drawable.no_image)
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
            color = Color.White,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            album.artist.name,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier
                .wrapContentSize(),
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
            painter = painterResource(R.drawable.no_image),
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