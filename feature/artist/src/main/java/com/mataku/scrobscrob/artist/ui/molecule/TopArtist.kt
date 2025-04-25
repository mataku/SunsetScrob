package com.mataku.scrobscrob.artist.ui.molecule

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
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
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.ui_common.R
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage

@Composable
internal fun TopArtist(
  sharedTransitionScope: SharedTransitionScope,
  animatedContentScope: AnimatedContentScope,
  id: String,
  artist: TopArtistInfo,
  imageUrl: String,
  onArtistTap: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .padding(
        start = 8.dp,
        end = 8.dp,
        bottom = 24.dp,
      )
      .clickable {
        onArtistTap()
      }
  ) {
    with(sharedTransitionScope) {
      SunsetImage(
        imageData = imageUrl,
        contentDescription = artist.name,
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
                  animatedVisibilityScope = animatedContentScope,
                  renderInOverlayDuringTransition = false,
                )
            }
          )
          .fillMaxWidth()
          .aspectRatio(1F),
        contentScale = ContentScale.FillWidth
      )
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text(
      artist.name,
      modifier = Modifier,
      style = SunsetTextStyle.body,
      fontWeight = FontWeight.Medium,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    val playCountResource = if (artist.playCount == "1") {
      R.string.playcount
    } else {
      R.string.playcounts
    }
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      stringResource(playCountResource, artist.playCount),
      style = SunsetTextStyle.caption,
      color = MaterialTheme.colorScheme.onSecondary,
      maxLines = 1
    )
  }
}
