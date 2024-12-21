package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.core.entity.presentation.toReadableIntValue
import com.mataku.scrobscrob.scrobble.ui.component.TrackDetail
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.component.ArtworkLayerBar
import com.mataku.scrobscrob.ui_common.component.CircleBackButton
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.molecule.ValueDescription
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TrackScreen(
  animatedContentScope: AnimatedContentScope,
  id: String,
  trackName: String,
  artistName: String,
  artworkUrl: String?,
  trackViewModel: TrackViewModel,
  navigateToWebView: (String) -> Unit,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by trackViewModel.state.collectAsStateWithLifecycle()

  TrackContent(
    animatedContentScope = animatedContentScope,
    id = id,
    artworkUrl = artworkUrl,
    trackInfo = uiState.trackInfo,
    onUrlTap = navigateToWebView,
    artistName = artistName,
    trackName = trackName,
    onBackPressed = onBackPressed,
    modifier = modifier
  )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.TrackContent(
  animatedContentScope: AnimatedContentScope,
  id: String,
  artworkUrl: String?,
  trackName: String,
  artistName: String,
  trackInfo: TrackInfo?,
  onUrlTap: (String) -> Unit,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier
) {
  val screenWidth = LocalConfiguration.current.screenWidthDp
  val screenHeight = LocalConfiguration.current.screenHeightDp
  val scaffoldState = rememberBottomSheetScaffoldState(
    bottomSheetState = rememberStandardBottomSheetState(
      initialValue = SheetValue.PartiallyExpanded
    )
  )
  BottomSheetScaffold(
    modifier = modifier,
    sheetContainerColor = MaterialTheme.colorScheme.background,
    scaffoldState = scaffoldState,
    sheetContent = {
      TrackDetailContent(
        trackInfo = trackInfo,
        trackName = trackName,
        artistName = artistName,
        onUrlTap = onUrlTap,
        modifier = Modifier
          .defaultMinSize(minHeight = screenWidth.dp)
          .fillMaxHeight(fraction = 0.9F)
      )
    },
    sheetPeekHeight = if (screenHeight >= screenWidth) {
      (screenHeight - screenWidth + 24).dp
    } else {
      (screenWidth - screenHeight).dp
    },
    content = {
      Column(
        modifier = Modifier
      ) {
        Box {
          SunsetImage(
            imageData = artworkUrl,
            contentDescription = "artwork image",
            modifier = Modifier
              .then(
                if (id.isEmpty() || LocalInspectionMode.current) {
                  Modifier
                } else {
                  Modifier
                    .sharedElement(
                      state = this@TrackContent.rememberSharedContentState(
                        key = id
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
          if (!this@TrackContent.isTransitionActive) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .background(
                  color = Color.Transparent
                )
            ) {
              ArtworkLayerBar()
              CircleBackButton(
                modifier = Modifier
                  .padding(
                    start = 4.dp
                  )
                  .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                  ) {
                    onBackPressed.invoke()
                  }
              )

            }
          }
        }
        SunsetImage(
          imageData = artworkUrl,
          contentDescription = "artwork_image_behind",
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1F),
          contentScale = ContentScale.FillWidth
        )
      }
    }
  )
}

@Composable
private fun TrackDetailContent(
  trackInfo: TrackInfo?,
  trackName: String,
  artistName: String,
  onUrlTap: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .verticalScroll(rememberScrollState())
  ) {
    if (trackInfo == null) {
      TrackDetail2(
        trackName = trackName,
        artistName = artistName,
        modifier = Modifier
          .padding(
            16.dp
          ),
      )
    } else {
      TrackDetail(
        trackInfo = trackInfo,
        modifier = Modifier
          .padding(
            vertical = 16.dp
          ),
        onUrlTap = onUrlTap
      )
    }

    Spacer(modifier = Modifier.height(32.dp))
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Preview(showBackground = true)
private fun TrackContentPreview() {
  SunsetThemePreview {
    Surface {
      SharedTransitionLayout {
        AnimatedContent(targetState = "", label = "track_screen_preview") {
          TrackContent(
            artworkUrl = it,
            trackInfo = TrackInfo(
              artist = com.mataku.scrobscrob.core.entity.TrackArtist(
                name = "aespaaespaaespaaespaaespaaespaaespa",
                url = ""
              ),
              listeners = "100000",
              url = "https://example.com",
              name = "Drama",
              album = TrackAlbumInfo(
                artist = "aespaaespaaespaaespaaespaaespa",
                imageList = persistentListOf(),
                title = "Drama"
              ),
              playCount = "10000",
              topTags = persistentListOf(
                Tag(
                  name = "K-POP",
                  url = ""
                ),
                Tag(
                  name = "K-POP",
                  url = ""
                ),
                Tag(
                  name = "K-POP",
                  url = ""
                )
              ),
              wiki = Wiki(
                published = "01 January 2023",
                content = "\"Clocks\" emerged in <b>conception during the late</b>stages into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. With this, they thought it was too late for the song's inclusion in the albumclude contrast, contradictions and urgency. Chris Martin sings of being in the state of \"helplessness ...",
                summary = "\"Clocks\" emerged in <b>conception during the late stages</b> into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. <a href=\"http://www.last.fm/music/Coldplay/_/Clocks\">Read more on Last.fm</a>.",
              ),
              userPlayCount = "10000"
            ),
            onUrlTap = {},
            artistName = "aespa",
            trackName = "Drama",
            onBackPressed = {},
            id = "123",
            animatedContentScope = this
          )
        }
      }
    }
  }
}

@Composable
private fun TrackDetail2(
  trackName: String,
  artistName: String,
  modifier: Modifier = Modifier,
  listeners: String? = null,
  playCount: String? = null,
) {
  Row(
    modifier = modifier
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = Modifier
        .weight(1F)
    ) {
      Text(
        text = trackName,
        style = SunsetTextStyle.body.copy(
          fontWeight = FontWeight.Bold
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = artistName,
        style = SunsetTextStyle.caption,
        modifier = Modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }

    Spacer(
      modifier = Modifier
        .width(32.dp)
    )

    TrackMetaData(
      listeners = listeners,
      playCount = playCount,
      modifier = Modifier
    )
  }
}

@Composable
private fun TrackMetaData(
  listeners: String?,
  playCount: String?,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(32.dp)
  ) {
    listeners?.let {
      ValueDescription(
        value = it.toReadableIntValue(),
        label = "Listeners",
      )
    }
    playCount?.let {
      ValueDescription(
        value = it.toReadableIntValue(),
        label = "Plays"
      )
    }
  }
}
