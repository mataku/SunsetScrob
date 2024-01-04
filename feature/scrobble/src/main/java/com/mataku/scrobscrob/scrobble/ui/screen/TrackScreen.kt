package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.scrobble.ui.molecule.TrackAlbum
import com.mataku.scrobscrob.scrobble.ui.molecule.TrackArtist
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.molecule.TopTags
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackScreen(
  trackName: String,
  artistName: String,
  artworkUrl: String?,
  trackViewModel: TrackViewModel,
) {
  val uiState by trackViewModel.state.collectAsState()

  val lazyListState = rememberLazyListState()
  val systemUiController = rememberSystemUiController()
  val currentTheme = LocalAppTheme.current
  systemUiController.setNavigationBarColor(
    color = currentTheme.backgroundColor()
  )

  LazyColumn(
    content = {
      stickyHeader {
        ContentHeader(text = trackName)
      }
      item {
        val imageData = if (artworkUrl == null || artworkUrl.isBlank()) {
          com.mataku.scrobscrob.ui_common.R.drawable.no_image
        } else {
          artworkUrl
        }

        SunsetImage(
          imageData = imageData,
          contentDescription = "artwork image",
          modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .aspectRatio(1F)
        )
      }
      val artistInfo = uiState.artistInfo
      val trackInfo = uiState.trackInfo

      if (uiState.isLoading) {
        item {
          Box(
            modifier = Modifier.fillMaxWidth()
          ) {
            CircularProgressIndicator(
              modifier = Modifier
                .size(40.dp)
                .padding(vertical = 16.dp)
                .align(Alignment.Center)
            )
          }
        }
      }

      artistInfo?.let { artist ->
        item {
          Divider()
          Spacer(modifier = Modifier.height(16.dp))
          TrackArtist(artistInfo = artist)
        }
      }

      trackInfo?.let { track ->
        track.album?.let { album ->
          item {
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            TrackAlbum(album = album)
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            TopTags(tagList = track.topTags)
          }
        }
      }
    },
    state = lazyListState,
    modifier = Modifier
      .fillMaxSize()
  )
}
