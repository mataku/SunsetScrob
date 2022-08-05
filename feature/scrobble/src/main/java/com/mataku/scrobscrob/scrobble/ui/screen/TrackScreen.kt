package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.scrobble.ui.molecule.TrackAlbum
import com.mataku.scrobscrob.scrobble.ui.molecule.TrackArtist
import com.mataku.scrobscrob.scrobble.ui.state.TrackScreenState
import com.mataku.scrobscrob.ui_common.molecule.TopTags
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.ANIMATION_DURATION_MILLIS
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackScreen(
  trackName: String,
  artistName: String,
  artworkUrl: String?,
  topLeftCoordinate: Pair<Int, Int>,
  screenState: TrackScreenState,
  onBackPressed: () -> Unit,
  onDispose: () -> Unit
) {
  // TODO: Replace with LocalDensity
  val density = LocalContext.current.resources.displayMetrics.density
  val widthPixels = LocalContext.current.resources.displayMetrics.widthPixels
  val screenWidthDp = (widthPixels / density).toInt()
  val animateState = remember {
    Animatable(0F, 1F)
  }
  val coroutineScope = rememberCoroutineScope()
  val lazyListState = rememberLazyListState()
  val uiState = screenState.uiState
  val loaded = remember {
    mutableStateOf(false)
  }
  if (!loaded.value) {
    screenState.fetchTrackInfo(
      trackName, artistName
    )
    loaded.value = true
  }

  LazyColumn(
    content = {
      stickyHeader {
        ContentHeader(text = trackName)
      }
      item {
        val animateValue = animateState.value
        val yCoordinate = topLeftCoordinate.second
        val offset = (yCoordinate.dp) - ((yCoordinate.dp) * animateValue)
        val imageSize = 48
        val horizontalPadding = 32
        val size =
          imageSize.dp + ((screenWidthDp - imageSize - horizontalPadding) * animateValue).dp

        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .size(1000)
            .data(artworkUrl ?: R.drawable.no_image)
            .build(),
          contentDescription = "artwork image",
          modifier = Modifier.then(
            Modifier
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .absoluteOffset(
                0.dp,
                offset
              )
              .size(size)
          )
        )
      }

      uiState.artistInfo?.let { artistInfo ->
        item {
          Divider()
          Spacer(modifier = Modifier.height(16.dp))
          TrackArtist(artistInfo = artistInfo)
        }
      }

      uiState.trackInfo?.let { trackInfo ->
        trackInfo.album?.let { album ->
          item {
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            TrackAlbum(album = album)
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            TopTags(tagList = trackInfo.topTags)
          }
        }
      }
    },
    state = lazyListState,
    modifier = Modifier
      .fillMaxSize()
      .padding(bottom = 56.dp)
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
      screenState.clearState()
      onDispose.invoke()
      animateState.animateTo(
        0F,
        animationSpec = tween(durationMillis = ANIMATION_DURATION_MILLIS)
      )
      onBackPressed.invoke()
    }
  }
}
