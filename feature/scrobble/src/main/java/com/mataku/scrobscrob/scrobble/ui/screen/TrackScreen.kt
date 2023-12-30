package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.scrobble.ui.molecule.TrackAlbum
import com.mataku.scrobscrob.scrobble.ui.molecule.TrackArtist
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.molecule.TopTags
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.ANIMATION_DURATION_MILLIS
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackScreen(
  trackName: String,
  artworkUrl: String?,
  topLeftCoordinate: Pair<Int, Int>,
  trackViewModel: TrackViewModel,
  navController: NavController
) {
  val uiState by trackViewModel.state.collectAsState()

  // TODO: Replace with LocalDensity
  val density = LocalContext.current.resources.displayMetrics.density
  val widthPixels = LocalContext.current.resources.displayMetrics.widthPixels
  val screenWidthDp = (widthPixels / density).toInt()
  val animateState = remember {
    Animatable(0F, 1F)
  }
  val coroutineScope = rememberCoroutineScope()
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
        val animateValue = animateState.value
        val yCoordinate = topLeftCoordinate.second
        val offset = (yCoordinate.dp) - ((yCoordinate.dp) * animateValue)
        val imageSize = 56
        val horizontalPadding = 32
        val size =
          imageSize.dp + ((screenWidthDp - imageSize - horizontalPadding) * animateValue).dp

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
            .absoluteOffset(
              0.dp,
              offset
            )
            .size(size)
        )
      }
      val artistInfo = uiState.artistInfo
      val trackInfo = uiState.trackInfo

      if (uiState.isLoading && animateState.value == 1F) {
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

      if (animateState.value == 1F) {
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
      }
    },
    state = lazyListState,
    modifier = Modifier
      .fillMaxSize()
  )

  LaunchedEffect(Unit) {
    coroutineScope.launch {
      animateState.animateTo(
        1F,
        animationSpec = tween(durationMillis = ANIMATION_DURATION_MILLIS)
      )
    }
  }

  BackHandler() {
    coroutineScope.launch {
      trackViewModel.clearState()
      animateState.animateTo(
        0F,
        animationSpec = tween(durationMillis = ANIMATION_DURATION_MILLIS)
      )
      navController.popBackStack()
    }
  }
}
