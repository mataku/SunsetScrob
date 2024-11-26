package com.mataku.scrobscrob.scrobble.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun Scrobble(recentTrack: RecentTrack, onScrobbleTap: () -> Unit) {
  ScrobbleContent(
    imageUrl = recentTrack.images.imageUrl(),
    trackName = recentTrack.name,
    artistName = recentTrack.artistName,
    date = recentTrack.date,
    onScrobbleTap = onScrobbleTap
  )
}

@Composable
private fun ScrobbleContent(
  imageUrl: String?,
  trackName: String,
  artistName: String,
  date: String?,
  onScrobbleTap: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(72.dp)
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
      SunsetImage(
        imageData = imageUrl,
        contentDescription = "$trackName artwork image",
        modifier = Modifier.size(56.dp),
      )

      Column(
        modifier = Modifier
          .padding(start = 16.dp)
          .fillMaxWidth()
          .height(56.dp),
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = trackName,
          modifier = Modifier.wrapContentSize(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = SunsetTextStyle.body.copy(
            fontWeight = FontWeight.Medium
          )
        )

        Spacer(modifier = Modifier.size(4.dp))

        Text(
          text = artistName,
          modifier = Modifier.wrapContentSize(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = SunsetTextStyle.caption
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
private fun ScrobblePreview() {
  SunsetThemePreview {
    Surface {
      ScrobbleContent(
        imageUrl = null,
        trackName = "裸足でSummer",
        artistName = "乃木坂46",
        date = "01 Aug 2022, 04:08",
        onScrobbleTap = {}
      )
    }
  }
}
