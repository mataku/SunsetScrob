package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.scrobble.R
import com.mataku.scrobscrob.scrobble.ui.molecule.Scrobble
import com.mataku.scrobscrob.scrobble.ui.state.ScrobbleScreenState
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
  SwipeRefresh(
    state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
    onRefresh = {
      state.refresh()
    }) {
    ScrobbleContent(
      recentTracks = uiState.recentTracks,
      hasNext = uiState.hasNext,
      onScrobbleTap = {
        state.onScrobbleTap(it)
      },
      onScrollEnd = {
        state.onScrollEnd()
      }
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScrobbleContent(
  recentTracks: List<RecentTrack>,
  hasNext: Boolean,
  onScrobbleTap: (String) -> Unit,
  onScrollEnd: () -> Unit
) {

  LazyColumn(
    content = {
      stickyHeader {
        ContentHeader(text = stringResource(id = R.string.menu_scrobble))
      }

      items(recentTracks) {
        Scrobble(
          recentTrack = it,
          onScrobbleTap = {
            onScrobbleTap(it.url)
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
    } else {
      Modifier
        .fillMaxSize()
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
