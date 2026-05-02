package com.mataku.scrobscrob.scrobble.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.mataku.scrobscrob.scrobble.ui.navigation.TrackDetailKey
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.ui_common.component.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.component.LoadingIndicator
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetListDetailScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetPullToRefreshBox
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBarScrollBehavior
import com.mataku.scrobscrob.ui_common.component.designsystem.rememberSunsetListDetailScaffoldState
import com.mataku.scrobscrob.ui_common.style.isCompactWidth
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ScrobbleScreen(
  sharedTransitionScope: SharedTransitionScope,
  animatedContentScope: AnimatedContentScope,
  viewModel: ScrobbleViewModel,
  topAppBarScrollBehavior: SunsetTopAppBarScrollBehavior,
  navigateToTrackDetail: (RecentTrack, String) -> Unit,
  trackViewModelProvider: @Composable (TrackDetailKey) -> TrackViewModel,
  navigateToWebView: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val lazyListState = rememberLazyListState()

  Box(modifier = modifier.fillMaxSize()) {
    if (isCompactWidth()) {
      SunsetPullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::refresh,
        modifier = Modifier.fillMaxSize()
      ) {
        ScrobbleContent(
          lazyListState = lazyListState,
          recentTracks = uiState.recentTracks,
          hasNext = uiState.hasNext,
          onScrobbleTap = navigateToTrackDetail,
          onScrollEnd = viewModel::fetchRecentTracks,
          scrollBehavior = topAppBarScrollBehavior,
          sharedTransitionScope = sharedTransitionScope,
          animatedVisibilityScope = animatedContentScope,
        )
      }
    } else {
      val scaffoldState = rememberSunsetListDetailScaffoldState<TrackDetailKey>()
      SunsetListDetailScaffold(
        state = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        listPane = {
          val listPaneScope: AnimatedVisibilityScope = this
          SunsetPullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier.fillMaxSize()
          ) {
            ScrobbleContent(
              lazyListState = lazyListState,
              recentTracks = uiState.recentTracks,
              hasNext = uiState.hasNext,
              onScrobbleTap = { track, id ->
                scaffoldState.selectDetail(
                  TrackDetailKey(
                    trackName = track.name,
                    artistName = track.artistName,
                    imageUrl = track.images.imageUrl() ?: "",
                    id = id,
                  )
                )
              },
              onScrollEnd = viewModel::fetchRecentTracks,
              scrollBehavior = topAppBarScrollBehavior,
              sharedTransitionScope = sharedTransitionScope,
              animatedVisibilityScope = listPaneScope,
              useSharedElement = false,
            )
          }
        },
        detailPane = { selection: TrackDetailKey? ->
          val detailPaneScope: AnimatedVisibilityScope = this
          if (selection != null) {
            with(sharedTransitionScope) {
              TrackPaneScreen(
                animatedVisibilityScope = detailPaneScope,
                id = "",
                trackName = selection.trackName,
                artistName = selection.artistName,
                artworkUrl = selection.imageUrl,
                trackViewModel = trackViewModelProvider(selection),
                navigateToWebView = navigateToWebView,
                onBackPressed = { scaffoldState.back() },
              )
            }
          }
        },
      )
    }

    if (uiState.isLoading && uiState.recentTracks.isEmpty()) {
      LoadingIndicator(modifier = Modifier.align(Alignment.Center))
    }
  }
}

@Composable
private fun ScrobbleContent(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  lazyListState: LazyListState,
  recentTracks: ImmutableList<RecentTrack>,
  hasNext: Boolean,
  onScrobbleTap: (RecentTrack, String) -> Unit,
  onScrollEnd: () -> Unit,
  scrollBehavior: SunsetTopAppBarScrollBehavior,
  useSharedElement: Boolean = true,
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
          "scrobble_${index}${track.hashCode()}"
        }
        val sharedElementId = if (useSharedElement) id else ""
        Scrobble(
          recentTrack = track,
          onScrobbleTap = {
            onScrobbleTap(
              track, id
            )
          },
          sharedTransitionScope = sharedTransitionScope,
          animatedVisibilityScope = animatedVisibilityScope,
          id = sharedElementId,
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
