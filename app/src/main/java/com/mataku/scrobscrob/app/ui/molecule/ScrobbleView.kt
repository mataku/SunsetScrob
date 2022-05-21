package com.mataku.scrobscrob.app.ui.molecule

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.ui_common.R as uiCommonR

@Composable
fun ScrobbleView(recentTrack: RecentTrack, onScrobbleTap: () -> Unit) {
    ScrobbleViewContent(
        imageUrl = recentTrack.largeImageUrl(),
        trackName = recentTrack.name,
        artistName = recentTrack.artist.name,
        onScrobbleTap = onScrobbleTap
    )
}

@Composable
private fun ScrobbleViewContent(
    imageUrl: String?,
    trackName: String,
    artistName: String,
    onScrobbleTap: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onScrobbleTap()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        val painter = if (imageUrl == null) {
            painterResource(uiCommonR.drawable.no_image)
        } else {
            rememberImagePainter(imageUrl)
        }
        Image(
            painter = painter,
            contentDescription = "Scrobble track image",
            modifier = Modifier.size(48.dp)
        )

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth()
                .height(48.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = trackName,
                fontSize = 14.sp,
                modifier = Modifier.wrapContentSize(),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = artistName,
                fontSize = 12.sp,
                modifier = Modifier.wrapContentSize(),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun ScrobbleViewPreview() {
    ScrobbleViewContent(
        imageUrl = null,
        trackName = "裸足でSummer",
        artistName = "乃木坂46",
        onScrobbleTap = {}
    )
}