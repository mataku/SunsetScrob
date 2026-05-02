package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.artist.ui.molecule.ArtistDetail
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.Stats
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.ui_common.component.ArtworkLayerBar
import com.mataku.scrobscrob.ui_common.component.CircleBackButton
import com.mataku.scrobscrob.ui_common.component.TopTags
import com.mataku.scrobscrob.ui_common.component.WikiCell
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetBottomSheet
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetHorizontalDivider
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetImage
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun SharedTransitionScope.ArtistScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  id: String,
  viewModel: ArtistViewModel,
  onArtistLoadMoreTap: (String) -> Unit,
  onBackPressed: () -> Unit
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  ArtistContent(
    artworkUrl = uiState.preloadArtworkUrl,
    artistName = uiState.preloadArtistName,
    artistInfo = uiState.artistInfo,
    onArtistLoadMoreTap = onArtistLoadMoreTap,
    onBackPressed = onBackPressed,
    animatedVisibilityScope = animatedVisibilityScope,
    id = id
  )
}

@Composable
private fun SharedTransitionScope.ArtistContent(
  animatedVisibilityScope: AnimatedVisibilityScope,
  id: String,
  artworkUrl: String,
  artistName: String,
  artistInfo: ArtistInfo?,
  onArtistLoadMoreTap: (String) -> Unit,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier
) {
  val screenWidth = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.width.toDp()
  }
  val screenHeight = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.height.toDp()
  }

  SunsetBottomSheet(
    modifier = modifier,
    sheetPeekHeight = if (screenHeight >= screenWidth) {
      (screenHeight - screenWidth + 24.dp)
    } else {
      (screenWidth - screenHeight)
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
                      sharedContentState = this@ArtistContent.rememberSharedContentState(
                        key = id
                      ),
                      animatedVisibilityScope = animatedVisibilityScope,
                      renderInOverlayDuringTransition = false,
                    )
                }
              )
              .fillMaxWidth()
              .aspectRatio(1F),
            contentScale = ContentScale.FillWidth,
            skipCrossFade = true
          )
          if (!this@ArtistContent.isTransitionActive) {
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
                    start = 4.dp,
                    top = 16.dp
                  )
                  .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                  ) {
                    onBackPressed.invoke()
                  }
                  .offset {
                    IntOffset(
                      x = 0,
                      y = (24.dp).value.toInt(),
                    )
                  }
              )
            }
          }
        }
        SunsetImage(
          imageData = artworkUrl,
          contentDescription = "artwork image",
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1F),
          contentScale = ContentScale.FillWidth,
          skipCrossFade = true
        )
      }
    },
    sheetContent = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight(fraction = 0.9F)
          .verticalScroll(rememberScrollState())
      ) {
        val stats = artistInfo?.stats
        ArtistDetail(
          artistName = artistName,
          listeners = stats?.listeners,
          playCount = stats?.playCount,
          modifier = Modifier
            .padding(16.dp)
        )

        artistInfo?.let { artistInfo ->
          TopTags(
            tagList = artistInfo.tags,
            modifier = Modifier.padding(
              vertical = 16.dp
            )
          )
          SunsetHorizontalDivider()

          artistInfo.wiki?.let {
            WikiCell(
              wiki = it,
              name = artistName,
              onUrlTap = onArtistLoadMoreTap,
              modifier = Modifier
                .padding(16.dp)
            )
          }
        }
      }
    },
  )
}

@Composable
internal fun SharedTransitionScope.ArtistPaneScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  id: String,
  viewModel: ArtistViewModel,
  onArtistLoadMoreTap: (String) -> Unit,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  ArtistPaneContent(
    animatedVisibilityScope = animatedVisibilityScope,
    id = id,
    artworkUrl = uiState.preloadArtworkUrl,
    artistName = uiState.preloadArtistName,
    artistInfo = uiState.artistInfo,
    onArtistLoadMoreTap = onArtistLoadMoreTap,
    onBackPressed = onBackPressed,
    modifier = modifier,
  )
}

@Composable
private fun SharedTransitionScope.ArtistPaneContent(
  animatedVisibilityScope: AnimatedVisibilityScope,
  id: String,
  artworkUrl: String,
  artistName: String,
  artistInfo: ArtistInfo?,
  onArtistLoadMoreTap: (String) -> Unit,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier,
) {
  SunsetBottomSheet(
    modifier = modifier,
    sheetPeekHeight = 280.dp,
    sheetContainerColor = LocalAppTheme.current.backgroundColor().copy(alpha = 0.85f),
    sheetContent = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight(fraction = 0.9F)
          .verticalScroll(rememberScrollState())
      ) {
        val stats = artistInfo?.stats
        ArtistDetail(
          artistName = artistName,
          listeners = stats?.listeners,
          playCount = stats?.playCount,
          modifier = Modifier.padding(16.dp),
        )

        artistInfo?.let { info ->
          TopTags(
            tagList = info.tags,
            modifier = Modifier.padding(vertical = 16.dp),
          )
          SunsetHorizontalDivider()

          info.wiki?.let {
            WikiCell(
              wiki = it,
              name = artistName,
              onUrlTap = onArtistLoadMoreTap,
              modifier = Modifier.padding(16.dp),
            )
          }
        }
      }
    },
    content = {
      Box(modifier = Modifier.fillMaxSize()) {
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
                    sharedContentState = this@ArtistPaneContent.rememberSharedContentState(
                      key = id
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    renderInOverlayDuringTransition = false,
                  )
              }
            )
            .fillMaxWidth()
            .aspectRatio(1F),
          contentScale = ContentScale.FillWidth,
          skipCrossFade = true,
        )
        if (!this@ArtistPaneContent.isTransitionActive) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(color = Color.Transparent)
          ) {
            ArtworkLayerBar()
            CircleBackButton(
              modifier = Modifier
                .padding(start = 4.dp, top = 16.dp)
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
    },
  )
}

@Composable
@Preview(showBackground = true)
private fun ArtistContentPreview() {
  SunsetThemePreview {
    SharedTransitionLayout {
      AnimatedContent(
        targetState = "",
        label = "artist_content_preview"
      ) {
        ArtistContent(
          artworkUrl = it,
          artistName = "aespa",
          artistInfo = ArtistInfo(
            name = "aespa",
            images = persistentListOf(),
            stats = Stats(
              listeners = "100000",
              playCount = "1000000"
            ),
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
              )
            ),
            wiki = Wiki(
              published = "01 January 2023",
              summary = LoremIpsum(100).values.joinToString(separator = " "),
              content = ""
            )
          ),
          onArtistLoadMoreTap = {},
          onBackPressed = {},
          id = "",
          animatedVisibilityScope = this
        )
      }
    }
  }
}
