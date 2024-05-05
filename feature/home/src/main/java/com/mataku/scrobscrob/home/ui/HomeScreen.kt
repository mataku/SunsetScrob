package com.mataku.scrobscrob.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.home.HomeTabType
import com.mataku.scrobscrob.home.ui.molecule.HomeTabs
import com.mataku.scrobscrob.scrobble.ui.screen.ScrobbleScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  navController: NavController
) {
  val pagerState = rememberPagerState(
    pageCount = {
      HomeTabType.entries.size
    }
  )
  val coroutineScope = rememberCoroutineScope()
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

  Scaffold(
    topBar = {
      SmallTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
          Text(text = "Home")
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
          scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
      )
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(it)
    ) {
      HomeTabs(
        selectedChartIndex = 0,
        onTabTap = { tabType ->
          coroutineScope.launch {
            pagerState.animateScrollToPage(tabType.ordinal)
          }
        },
        modifier = Modifier
          .background(
            MaterialTheme.colorScheme.background
          )
      )

      HorizontalPager(state = pagerState) { page ->
        val homeTabType = HomeTabType.findByIndex(page)
        when (homeTabType) {
          HomeTabType.SCROBBLE -> {
            ScrobbleScreen(
              viewModel = hiltViewModel(),
              navController = navController,
              scrollBehavior = scrollBehavior
            )
          }

          else -> Unit
        }
      }
    }
  }
}
