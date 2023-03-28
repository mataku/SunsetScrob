@file:OptIn(ExperimentalMaterialApi::class)

package com.mataku.scrobscrob.scrobble.ui.screen

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.scrobble.ui.molecule.Scrobble
import com.mataku.scrobscrob.scrobble.ui.state.ScrobbleScreenState
import com.mataku.scrobscrob.ui_common.molecule.LoadingIndicator
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.sunsetBackgroundGradient

@Composable
fun ScrobbleScreen(
  state: ScrobbleScreenState
) {
  val uiState = state.uiState
  val lazyListState = rememberLazyListState()
  val density = LocalContext.current.resources.displayMetrics.density

  val pullRefreshState = rememberPullRefreshState(
    refreshing = uiState.isRefreshing,
    onRefresh = {
      state.refresh()
    }
  )

  Box(
    modifier = Modifier.pullRefresh(
      state = pullRefreshState,
    )
  ) {
    ScrobbleContent(
      lazyListState = lazyListState,
      recentTracks = uiState.recentTracks,
      hasNext = uiState.hasNext,
      onScrobbleTap = { track, firstVisibleIndex, tappedItemIndex, firstVisibleItemScrollOffset ->
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

        state.navigateToTrackDetail(
          trackName = track.name,
          artistName = track.artistName,
          imageUrl = track.images.imageUrl() ?: "",
          x = topLeftCoordinate.first,
          y = topLeftCoordinate.second
        )
      },
      onScrollEnd = {
        state.onScrollEnd()
      }
    )
  }

  if (uiState.isLoading && uiState.recentTracks.isEmpty()) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      LoadingIndicator(modifier = Modifier)
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
      if (hasNext && recentTracks.isNotEmpty()) {
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
