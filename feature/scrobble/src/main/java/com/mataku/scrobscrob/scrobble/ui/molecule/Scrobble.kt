package com.mataku.scrobscrob.scrobble.ui.molecule

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.core.api.endpoint.RecentTrackDate
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.ui_common.R as uiCommonR

@Composable
fun Scrobble(recentTrack: RecentTrack, onScrobbleTap: () -> Unit) {
  ScrobbleContent(
    imageUrl = recentTrack.imageUrl(),
    trackName = recentTrack.name,
    artistName = recentTrack.artist.name,
    date = recentTrack.date,
    onScrobbleTap = onScrobbleTap
  )
}

@Composable
private fun ScrobbleContent(
  imageUrl: String?,
  trackName: String,
  artistName: String,
  date: RecentTrackDate?,
  onScrobbleTap: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp)
      .clickable {
        onScrobbleTap()
      }
      .padding(
        horizontal = 16.dp,
        vertical = 8.dp
      ),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(modifier = Modifier.weight(1F)) {
      if (imageUrl == null) {
        Image(
          painter = painterResource(uiCommonR.drawable.no_image),
          contentDescription = "Scrobble track image",
          modifier = Modifier.size(48.dp)
        )
      } else {
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .size(1000)
            .data(imageUrl)
            .build(), contentDescription = "$trackName artwork image",
          modifier = Modifier.size(48.dp)
        )
      }

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
          color = MaterialTheme.colors.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.size(4.dp))

        Text(
          text = artistName,
          fontSize = 12.sp,
          modifier = Modifier.wrapContentSize(),
          color = MaterialTheme.colors.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    if (date == null) {
      val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.equalizer))
      val animationState by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
      )
      LottieAnimation(
        composition = composition,
        progress = { animationState },
        modifier = Modifier.size(24.dp)
      )
    }
  }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun ScrobblePreview() {
  ScrobbleContent(
    imageUrl = null,
    trackName = "裸足でSummer",
    artistName = "乃木坂46",
    date = RecentTrackDate("01 Aug 2022, 04:08"),
    onScrobbleTap = {}
  )
}
