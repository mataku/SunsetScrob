package com.mataku.scrobscrob.discover.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.discover.ui.molecule.ChartArtistList
import com.mataku.scrobscrob.discover.ui.molecule.ChartTrackList
import com.mataku.scrobscrob.discover.ui.viewmodel.DiscoverViewModel
import com.mataku.scrobscrob.ui_common.navigateToWebView
import com.mataku.scrobscrob.ui_common.organism.ContentHeader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiscoverScreen(
  viewModel: DiscoverViewModel = hiltViewModel(),
  navController: NavController
) {
  val uiState by viewModel.uiState.collectAsState()

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    stickyHeader {
      ContentHeader(text = "Discover")
    }

    if (uiState.topTracks.isNotEmpty()) {
      item(
        key = "chart_track_list"
      ) {
        ChartTrackList(
          chartTrackList = uiState.topTracks,
          onChartTrackTap = {
            navController.navigateToWebView(it.url)
          }
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
            navController.navigateToWebView(it.url)
          }
        )
      }
    }
  }
}
