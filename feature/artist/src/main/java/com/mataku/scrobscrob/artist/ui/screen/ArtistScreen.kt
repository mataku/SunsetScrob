package com.mataku.scrobscrob.artist.ui.screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.artist.ui.molecule.ArtistDetail
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.Stats
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.ui_common.component.CircleBackButton
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.molecule.TopTags
import com.mataku.scrobscrob.ui_common.molecule.WikiCell
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ArtistScreen(
  viewModel: ArtistViewModel,
  onArtistLoadMoreTap: (String) -> Unit,
  onBackPressed: () -> Unit
) {
  val uiState by viewModel.uiState.collectAsState()

  ArtistContent(
    artworkUrl = uiState.preloadArtworkUrl,
    artistName = uiState.preloadArtistName,
    artistInfo = uiState.artistInfo,
    onArtistLoadMoreTap = onArtistLoadMoreTap,
    onBackPressed = onBackPressed
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArtistContent(
  artworkUrl: String,
  artistName: String,
  artistInfo: ArtistInfo?,
  onArtistLoadMoreTap: (String) -> Unit,
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
      (screenHeight - screenWidth).dp
    } else {
      (screenWidth - screenHeight).dp
    },
    content = {
      val imageData = artworkUrl.ifBlank {
        com.mataku.scrobscrob.ui_common.R.drawable.no_image
      }
      Column {
        Box {
          SunsetImage(
            imageData = imageData,
            contentDescription = "artwork image",
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(1F),
            contentScale = ContentScale.FillWidth
          )
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                color = Color.Transparent
              )
          ) {
            Spacer(modifier = Modifier.height(24.dp))
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
        SunsetImage(
          imageData = imageData,
          contentDescription = "artwork image",
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1F),
          contentScale = ContentScale.FillWidth
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
          HorizontalDivider()

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
@Preview(showBackground = true)
private fun ArtistContentPreview() {
  SunsetThemePreview {
    Surface {
      ArtistContent(
        artworkUrl = "",
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
        onBackPressed = {}
      )
    }
  }
}
