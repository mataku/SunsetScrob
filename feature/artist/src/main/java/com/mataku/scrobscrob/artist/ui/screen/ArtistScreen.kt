package com.mataku.scrobscrob.artist.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldDefaults
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ArtistContent(
  artworkUrl: String,
  artistName: String,
  artistInfo: ArtistInfo?,
  onArtistLoadMoreTap: (String) -> Unit,
  onBackPressed: () -> Unit
) {
  val screenWidth = LocalConfiguration.current.screenWidthDp
  val screenHeight = LocalConfiguration.current.screenHeightDp
  val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)

  BackdropScaffold(
    appBar = {},
    headerHeight = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
      (screenHeight - screenWidth).dp
    } else {
      BackdropScaffoldDefaults.HeaderHeight
    },
    backLayerContent = {
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
              .aspectRatio(1F)
          )
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(64.dp)
              .background(
                color = Color.Transparent
              )
          ) {
            Image(
              painter = rememberVectorPainter(image = Icons.AutoMirrored.Default.ArrowBack),
              contentDescription = "back",
              modifier = Modifier
                .clickable(
                  indication = null,
                  interactionSource = remember { MutableInteractionSource() }
                ) {
                  onBackPressed.invoke()
                }
                .padding(
                  16.dp
                )
                .alpha(0.9F),
              colorFilter = ColorFilter.tint(
                color = Color.Gray.copy(
                  alpha = 0.9F
                )
              )
            )
          }
        }
        SunsetImage(
          imageData = imageData,
          contentDescription = "artwork image",
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1F)
            .offset(
              y = (screenWidth / 2).dp
            )
        )
      }
    },
    frontLayerContent = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
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
    modifier = Modifier
      .fillMaxSize()
      .background(
        color = Color.White
      ),
    scaffoldState = scaffoldState,
    frontLayerBackgroundColor = MaterialTheme.colorScheme.background,
    frontLayerScrimColor = Color.Transparent
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
