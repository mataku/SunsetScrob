package com.mataku.scrobscrob.album.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.album.ui.molecule.AlbumMetaData
import com.mataku.scrobscrob.album.ui.molecule.AlbumTrackList
import com.mataku.scrobscrob.album.ui.viewmodel.AlbumViewModel
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.AlbumInfoTrack
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.ui_common.component.ArtworkLayerBar
import com.mataku.scrobscrob.ui_common.component.CircleBackButton
import com.mataku.scrobscrob.ui_common.molecule.SimpleWiki
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.molecule.TopTags
import com.mataku.scrobscrob.ui_common.molecule.WikiCell
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SharedTransitionScope.AlbumScreen(
  animatedContentScope: AnimatedContentScope,
  id: String,
  viewModel: AlbumViewModel,
  onAlbumLoadMoreTap: (String) -> Unit,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  AlbumContent(
    animatedContentScope = animatedContentScope,
    id = id,
    artworkUrl = uiState.preloadArtworkUrl,
    albumName = uiState.preloadAlbumName,
    artistName = uiState.preloadArtistName,
    albumInfo = uiState.albumInfo,
    onAlbumLoadMoreTap = onAlbumLoadMoreTap,
    onBackPressed = onBackPressed,
    modifier = modifier
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SharedTransitionScope.AlbumContent(
  animatedContentScope: AnimatedContentScope,
  id: String,
  artworkUrl: String,
  albumName: String,
  artistName: String,
  albumInfo: AlbumInfo?,
  onAlbumLoadMoreTap: (String) -> Unit,
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
    sheetPeekHeight = if (screenHeight >= screenWidth) {
      (screenHeight - screenWidth + 24).dp
    } else {
      (screenWidth - screenHeight).dp
    },
    content = {
      Column {
        Box {
          SunsetImage(
            imageData = artworkUrl,
            contentDescription = "artwork image",
            modifier = Modifier
              .then(
                if (id.isEmpty()) {
                  Modifier
                } else {
                  Modifier
                    .sharedElement(
                      state = this@AlbumContent.rememberSharedContentState(
                        key = id
                      ),
                      animatedVisibilityScope = animatedContentScope,
                      renderInOverlayDuringTransition = false,
                    )
                }
              )
              .fillMaxWidth()
              .aspectRatio(1F),
            contentScale = ContentScale.FillWidth,
            skipCrossFade = false
          )
          if (!this@AlbumContent.isTransitionActive) {
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
          contentScale = ContentScale.FillWidth,
          skipCrossFade = false
        )
      }
    },
    sheetContent = {
      AlbumDetailContent(
        albumName = albumName,
        artistName = artistName,
        albumInfo = albumInfo,
        onAlbumLoadMoreTap
      )
    }
  )
}

@Composable
private fun AlbumDetailContent(
  albumName: String,
  artistName: String,
  albumInfo: AlbumInfo?,
  onAlbumLoadMoreTap: (String) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(fraction = 0.9F)
      .verticalScroll(rememberScrollState())
  ) {
    AlbumMetaData(
      albumName = albumName,
      artistName = artistName,
      listeners = albumInfo?.listeners,
      playCount = albumInfo?.playCount,
      modifier = Modifier
        .padding(16.dp)
    )
    val tags = albumInfo?.tags
    if (tags?.isNotEmpty() == true) {
      TopTags(
        tagList = tags,
        modifier = Modifier
          .padding(
            vertical = 16.dp
          )
      )
    }
    HorizontalDivider()

    albumInfo?.let { album ->
      Spacer(modifier = Modifier.height(16.dp))
      AlbumTrackList(
        tracks = album.tracks,
        modifier = Modifier
          .padding(
            horizontal = 16.dp
          )
      )
      Spacer(modifier = Modifier.height(16.dp))
      HorizontalDivider()
      val wiki = album.wiki
      if (wiki == null) {
        SimpleWiki(
          name = albumName,
          url = album.url,
          onUrlTap = onAlbumLoadMoreTap,
          modifier = Modifier
            .padding(16.dp)
        )
      } else {
        WikiCell(
          wiki = wiki,
          name = albumName,
          modifier = Modifier
            .padding(
              16.dp
            ),
          onUrlTap = onAlbumLoadMoreTap
        )
      }
    }
    Spacer(modifier = Modifier.height(32.dp))
  }
}

@Composable
@Preview(showBackground = true)
private fun AlbumContentPreview() {
  SunsetThemePreview {
    Surface {
      SharedTransitionLayout {
        AnimatedContent(
          targetState = "",
          label = "album_content_preview"
        ) {
          AlbumContent(
            artworkUrl = it,
            albumName = "Drama",
            artistName = "aespa",
            albumInfo = AlbumInfo(
              artistName = "aespa",
              albumName = "Drama",
              tracks = persistentListOf(
                AlbumInfoTrack(
                  duration = "100",
                  name = "Drama",
                  url = ""
                ),
                AlbumInfoTrack(
                  duration = "100",
                  name = "Drama",
                  url = ""
                ),
                AlbumInfoTrack(
                  duration = "100",
                  name = "Drama",
                  url = ""
                )
              ),
              images = persistentListOf(),
              listeners = "10000",
              playCount = "1000",
              url = "",
              tags = persistentListOf(
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
                ),
                Tag(
                  name = "K-POP",
                  url = ""
                ),
              ),
              wiki = Wiki(
                published = "01 January 2023",
                content = "\"Clocks\" emerged in <b>conception during the late</b>stages into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. With this, they thought it was too late for the song's inclusion in the albumclude contrast, contradictions and urgency. Chris Martin sings of being in the state of \"helplessness ...",
                summary = "\"Clocks\" emerged in <b>conception during the late stages</b> into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. <a href=\"http://www.last.fm/music/Coldplay/_/Clocks\">Read more on Last.fm</a>.",
              )
            ),
            onAlbumLoadMoreTap = {},
            onBackPressed = {},
            animatedContentScope = this,
            id = ""
          )
        }
      }
    }
  }
}
