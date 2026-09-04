package com.mataku.scrobscrob.scrobble.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetImage
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun Scrobble(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  id: String,
  recentTrack: RecentTrack,
  onScrobbleTap: () -> Unit
) {
  ScrobbleContent(
    imageUrl = recentTrack.images.imageUrl(),
    trackName = recentTrack.name,
    artistName = recentTrack.artistName,
    date = recentTrack.date,
    onScrobbleTap = onScrobbleTap,
    sharedTransitionScope = sharedTransitionScope,
    animatedVisibilityScope = animatedVisibilityScope,
    id = id
  )
}

@Composable
private fun ScrobbleContent(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  id: String,
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
      with(sharedTransitionScope) {
        SunsetImage(
          skipCrossFade = false,
          imageData = imageUrl,
          contentDescription = "$trackName artwork image",
          modifier = Modifier
            .then(
              if (id.isEmpty()) {
                Modifier
              } else {
                Modifier
                  .sharedElement(
                    sharedContentState = sharedTransitionScope.rememberSharedContentState(
                      key = id,
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    renderInOverlayDuringTransition = false,
                  )
              }
            )
            .size(56.dp),
        )
      }

      Column(
        modifier = Modifier
          .padding(start = 16.dp)
          .fillMaxWidth()
          .height(56.dp),
        verticalArrangement = Arrangement.Center
      ) {
        SunsetText.Body(
          text = trackName,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.wrapContentSize(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.size(4.dp))

        SunsetText.Caption(
          text = artistName,
          modifier = Modifier.wrapContentSize(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }

    if (date == null) {
      EqualizerAnimation(modifier = Modifier.size(24.dp))
    }
  }
}

@Composable
@Preview(showBackground = true)
private fun ScrobblePreview() {
  SunsetThemePreview {
    SharedTransitionLayout {
      AnimatedContent(
        targetState = "",
        label = "scrobble_preview"
      ) {
        ScrobbleContent(
          imageUrl = it,
          trackName = "裸足でSummer",
          artistName = "乃木坂46",
          date = "01 Aug 2022, 04:08",
          onScrobbleTap = {},
          animatedVisibilityScope = this,
          sharedTransitionScope = this@SharedTransitionLayout,
          id = ""
        )
      }
    }
  }
}
