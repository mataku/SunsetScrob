package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.core.entity.isInvalidArtwork
import com.mataku.scrobscrob.scrobble.ui.component.Scrobble
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ScrobbleScreen(
  sharedTransitionScope: SharedTransitionScope,
  animatedContentScope: AnimatedContentScope,
  viewModel: ScrobbleViewModel,
  topAppBarScrollBehavior: TopAppBarScrollBehavior,
  navigateToTrackDetail: (RecentTrack, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val lazyListState = rememberLazyListState()

  PullToRefreshBox(
    isRefreshing = uiState.isRefreshing,
    onRefresh = viewModel::refresh,
    modifier = modifier.fillMaxSize()
  ) {
    ScrobbleContent(
      lazyListState = lazyListState,
      recentTracks = uiState.recentTracks,
      hasNext = uiState.hasNext,
      onScrobbleTap = navigateToTrackDetail,
      onScrollEnd = viewModel::fetchRecentTracks,
      scrollBehavior = topAppBarScrollBehavior,
      sharedTransitionScope = sharedTransitionScope,
      animatedContentScope = animatedContentScope
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun ScrobbleContent(
  sharedTransitionScope: SharedTransitionScope,
  animatedContentScope: AnimatedContentScope,
  lazyListState: LazyListState,
  recentTracks: ImmutableList<RecentTrack>,
  hasNext: Boolean,
  onScrobbleTap: (RecentTrack, String) -> Unit,
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
      ) { index, track ->
        val artwork = track.images.imageUrl()
        val id = if (artwork.isInvalidArtwork()) {
          ""
        } else {
          "${index}${track.hashCode()}"
        }
        Scrobble(
          recentTrack = track,
          onScrobbleTap = {
            onScrobbleTap(
              track, id
            )
          },
          sharedTransitionScope = sharedTransitionScope,
          animatedContentScope = animatedContentScope,
          id = id,
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
