package com.mataku.scrobscrob.discover.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.discover.ui.molecule.ChartArtistList
import com.mataku.scrobscrob.discover.ui.molecule.ChartTrackList
import com.mataku.scrobscrob.discover.ui.molecule.RecentlyLovedTrackList
import com.mataku.scrobscrob.discover.ui.viewmodel.DiscoverViewModel
import com.mataku.scrobscrob.ui_common.organism.ContentHeader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiscoverScreen(
  viewModel: DiscoverViewModel,
  navigateToWebView: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LazyColumn(
    modifier = modifier.fillMaxSize(),
  ) {
    stickyHeader {
      ContentHeader(text = "Discover")
    }

    if (uiState.recentlyLovedTracks.isNotEmpty()) {
      item(
        key = "recently_loved_track_list"
      ) {
        RecentlyLovedTrackList(
          lovedTrackList = uiState.recentlyLovedTracks,
          onLovedTrackTap = {
            navigateToWebView.invoke(it.url)
          },
        )
      }
    }

    if (uiState.topTracks.isNotEmpty()) {
      item(
        key = "chart_track_list"
      ) {
        ChartTrackList(
          chartTrackList = uiState.topTracks,
          onChartTrackTap = {
            navigateToWebView.invoke(it.url)
          },
          modifier = Modifier
            .padding(
              top = 32.dp
            )
        )
      }
    }

    if (uiState.topArtists.isNotEmpty()) {
      item(
        key = "chart_artist_list"
      ) {
        ChartArtistList(
          chartArtistList = uiState.topArtists,
          onChartArtistTap = {
            navigateToWebView.invoke(it.url)
          },
          modifier = Modifier
            .padding(
              top = 32.dp
            )
        )
      }
    }

    item(
      key = "discover_footer"
    ) {
      Spacer(modifier = Modifier.height(96.dp))
    }
  }
}
