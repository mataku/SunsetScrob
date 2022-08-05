package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.scrobble.ui.molecule.Scrobble
import com.mataku.scrobscrob.scrobble.ui.state.ScrobbleScreenState
import com.mataku.scrobscrob.scrobble.ui.state.TrackScreenState
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.ANIMATION_DURATION_MILLIS
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.sunsetBackgroundGradient
import kotlinx.coroutines.launch

@Composable
fun ScrobbleScreen(
  state: ScrobbleScreenState,
  trackScreenState: TrackScreenState
) {
  val uiState = state.uiState
  val detail = remember {
    mutableStateOf(false)
  }
  val item = remember {
    mutableStateOf(
      Pair<Pair<Int, Int>, RecentTrack?>(Pair(0, 0), null)
    )
  }
  val lazyListState = rememberLazyListState()
  val density = LocalContext.current.resources.displayMetrics.density
  val alphaValue = remember {
    Animatable(0F)
  }
  val coroutineScope = rememberCoroutineScope()
  if (detail.value) {
    LaunchedEffect(Unit) {
      coroutineScope.launch {
        alphaValue.animateTo(
          0F,
          animationSpec = tween(durationMillis = ANIMATION_DURATION_MILLIS)
        )
      }
    }
    val track = item.value.second!!
    TrackScreen(
      trackName = track.name,
      artistName = track.artistName,
      artworkUrl = track.images.imageUrl(),
      topLeftCoordinate = item.value.first,
      onBackPressed = {
        detail.value = false
      },
      onDispose = {
        coroutineScope.launch {
          alphaValue.animateTo(
            1F,
            animationSpec = tween(durationMillis = ANIMATION_DURATION_MILLIS)
          )
        }
      },
      screenState = trackScreenState
    )
  }

  val alpha = if (alphaValue.value < 0.9F) {
    0F
  } else {
    alphaValue.value
  }

  if (!detail.value || alpha >= 0.9F) {
    SwipeRefresh(
      modifier = Modifier.alpha(
        if (detail.value) {
          alpha
        } else {
          1F
        }
      ),
      state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
      onRefresh = {
        state.refresh()
      }) {
      ScrobbleContent(
        lazyListState = lazyListState,
        recentTracks = uiState.recentTracks,
        hasNext = uiState.hasNext,
        onScrobbleTap = { track, firstVisibleIndex, tappedItemIndex, firstVisibleItemScrollOffset ->
          if (detail.value) {
            return@ScrobbleContent
          }

          val topLeftCoordinate = if (firstVisibleIndex == tappedItemIndex) {
            Pair(0, 0)
          } else {
            val cellHeight = density * 64
            val betweenCellCount = tappedItemIndex - firstVisibleIndex - 1
            val heightPxBetweenTappedItemAndFirstVisibleItem = cellHeight * betweenCellCount
            val firstVisibleItemRemainingHeightPx = cellHeight - firstVisibleItemScrollOffset

            Pair(
              0,
              ((firstVisibleItemRemainingHeightPx + heightPxBetweenTappedItemAndFirstVisibleItem) / density).toInt()
            )
          }

          item.value = Pair(
            topLeftCoordinate,
            track
          )
          detail.value = true
        },
        onScrollEnd = {
          state.onScrollEnd()
        }
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScrobbleContent(
  lazyListState: LazyListState,
  recentTracks: List<RecentTrack>,
  hasNext: Boolean,
  onScrobbleTap: (RecentTrack, Int, Int, Int) -> Unit,
  onScrollEnd: () -> Unit
) {
  LazyColumn(
    state = lazyListState,
    content = {
      stickyHeader {
        ContentHeader(text = stringResource(id = R.string.menu_scrobble))
      }

      itemsIndexed(recentTracks) { index, track ->
        Scrobble(
          recentTrack = track,
          onScrobbleTap = {
            onScrobbleTap(
              track,
              lazyListState.firstVisibleItemIndex,
              index,
              lazyListState.firstVisibleItemScrollOffset
            )
          }
        )
      }
      if (hasNext) {
        item {
          Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            InfiniteLoadingIndicator(onScrollEnd = onScrollEnd)
          }
        }
      }
    },
    modifier = if (LocalAppTheme.current == AppTheme.SUNSET) {
      Modifier
        .fillMaxSize()
        .background(
          brush = sunsetBackgroundGradient
        )
        .padding(bottom = 56.dp)
    } else {
      Modifier
        .fillMaxSize()
        .padding(bottom = 56.dp)
    }
  )
}

@Preview(showBackground = true)
@Composable
private fun GradientPreview() {
  SunsetThemePreview {
    androidx.compose.material.Surface {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(Colors.SunsetOrange, Colors.SunsetBlue)
            )
          )
      )
    }
  }
}
