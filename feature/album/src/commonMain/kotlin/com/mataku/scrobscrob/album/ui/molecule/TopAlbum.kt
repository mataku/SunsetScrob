package com.mataku.scrobscrob.album.ui.molecule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetImage
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.onSecondaryColor
import com.mataku.scrobscrob.ui_common.generated.resources.Res
import com.mataku.scrobscrob.ui_common.generated.resources.playcount
import com.mataku.scrobscrob.ui_common.generated.resources.playcounts
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TopAlbum(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  id: String,
  album: TopAlbumInfo,
  onAlbumTap: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .fillMaxWidth()
      .padding(
        start = 8.dp,
        end = 8.dp,
        bottom = 24.dp
      )
      .clickable {
        onAlbumTap()
      },
  ) {
    val imageUrl = album.imageList.imageUrl()

    with(sharedTransitionScope) {
      SunsetImage(
        imageData = imageUrl,
        contentDescription = album.title,
        contentScale = ContentScale.FillWidth,
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
          .fillMaxWidth()
          .aspectRatio(1F)
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
    SunsetText.Body(
      text = album.title,
      fontWeight = FontWeight.Medium,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
    SunsetText.Label(
      text = album.artist,
      maxLines = 1,
    )
    Spacer(modifier = Modifier.height(2.dp))
    val playCountResource = if (album.playCount == "1") {
      Res.string.playcount
    } else {
      Res.string.playcounts
    }
    SunsetText.Caption(
      text = stringResource(playCountResource, album.playCount),
      color = LocalAppTheme.current.onSecondaryColor(),
      maxLines = 1
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun TopAlbumPreview() {
  SunsetThemePreview {
    SharedTransitionLayout {
      AnimatedContent(targetState = "", label = "top_album_preview") {
        TopAlbum(
          album = TopAlbumInfo(
            artist = "乃木坂46",
            title = "生まれてから初めて見た夢",
            imageList = persistentListOf(),
            playCount = "100000",
            url = ""
          ),
          onAlbumTap = {},
          modifier = Modifier,
          sharedTransitionScope = this@SharedTransitionLayout,
          animatedVisibilityScope = this,
          id = it
        )
      }
    }
  }
}
