package com.mataku.scrobscrob.chart.ui.screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.chart.ui.molecule.ChartArtistList
import com.mataku.scrobscrob.chart.ui.molecule.ChartTabRow
import com.mataku.scrobscrob.chart.ui.molecule.ChartTrackList
import com.mataku.scrobscrob.chart.ui.viewmodel.ChartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChartScreen(
  viewModel: ChartViewModel = hiltViewModel(),
  navController: NavController
) {
  val uiState by viewModel.uiState.collectAsState()
  val pagerState = rememberPagerState {
    2
  }
  val coroutineScope = rememberCoroutineScope()

  Column(
    modifier = Modifier
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    ChartTabRow(
      selectedChartIndex = pagerState.currentPage,
      onChartTypeTap = { index, chartType ->
        coroutineScope.launch {
          pagerState.animateScrollToPage(
            index,
            animationSpec = tween(500)
          )
        }
        viewModel.changeSelectedChartType(chartType)
      },
      modifier = Modifier
        .padding(
          vertical = 12.dp
        )
    )
    HorizontalPager(
      state = pagerState,
      pageContent = { page ->
        if (page == 0) {
          ChartArtistList(
            chartArtistList = uiState.topArtists,
            onChartArtistTap = {}
          )
        } else {
          ChartTrackList(
            chartTrackList = uiState.topTracks,
            onChartTrackTap = {}
          )
        }
      }
    )
  }
}
