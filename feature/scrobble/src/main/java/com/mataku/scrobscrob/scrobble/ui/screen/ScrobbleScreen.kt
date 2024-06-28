package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.scrobble.ui.molecule.Scrobble
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel
import com.mataku.scrobscrob.ui_common.molecule.LoadingIndicator
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScrobbleScreen(
  viewModel: ScrobbleViewModel,
  topAppBarScrollBehavior: TopAppBarScrollBehavior,
  navigateToTrackDetail: (RecentTrack) -> Unit
) {
  val uiState by viewModel.uiState.collectAsState()
  val lazyListState = rememberLazyListState()

  val pullRefreshState = rememberPullRefreshState(
    refreshing = uiState.isRefreshing,
    onRefresh = viewModel::refresh
  )

  Box(
    modifier = Modifier
      .pullRefresh(
        state = pullRefreshState,
      )
      .fillMaxSize()
  ) {
    ScrobbleContent(
      lazyListState = lazyListState,
      recentTracks = uiState.recentTracks,
      hasNext = uiState.hasNext,
      onScrobbleTap = navigateToTrackDetail,
      onScrollEnd = viewModel::fetchRecentTracks,
      scrollBehavior = topAppBarScrollBehavior
    )

    PullRefreshIndicator(
      refreshing = uiState.isRefreshing,
      state = pullRefreshState,
      modifier = Modifier.align(alignment = Alignment.TopCenter)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScrobbleContent(
  lazyListState: LazyListState,
  recentTracks: ImmutableList<RecentTrack>,
  hasNext: Boolean,
  onScrobbleTap: (RecentTrack) -> Unit,
  onScrollEnd: () -> Unit,
  scrollBehavior: TopAppBarScrollBehavior
) {
  LazyColumn(
    state = lazyListState,
    content = {
      itemsIndexed(
        items = recentTracks,
        key = { index, track ->
          "${index}${track.hashCode()}"
        },
        contentType = { _, _ ->
          "scrobble"
        }
      ) { _, track ->
        Scrobble(
          recentTrack = track,
          onScrobbleTap = {
            onScrobbleTap(
              track
            )
          }
        )
      }
      if (hasNext && recentTracks.isNotEmpty()) {
        item(
          key = "footer_loading",
          contentType = "footer_loading"
        ) {
          Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            InfiniteLoadingIndicator(onScrollEnd = onScrollEnd)
          }
        }
      }
    },
    modifier = Modifier
      .fillMaxSize()
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    contentPadding = PaddingValues(
      vertical = 8.dp
    )
  )
}
