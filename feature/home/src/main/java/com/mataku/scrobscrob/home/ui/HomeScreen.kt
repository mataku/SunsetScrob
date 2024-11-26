package com.mataku.scrobscrob.home.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.hilt.navigation.compose.hiltViewModel
import com.mataku.scrobscrob.album.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.artist.ui.screen.TopArtistsScreen
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.home.HomeTabType
import com.mataku.scrobscrob.home.ui.molecule.HomeTabs
import com.mataku.scrobscrob.scrobble.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.ui_common.style.LocalTopAppBarState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  navigateToTrackDetail: (RecentTrack) -> Unit,
  navigateToArtistDetail: (TopArtistInfo) -> Unit,
  navigateToAlbumDetail: (TopAlbumInfo) -> Unit,
  modifier: Modifier = Modifier
) {
  val pagerState = rememberPagerState(
    pageCount = {
      HomeTabType.entries.size
    }
  )
  val coroutineScope = rememberCoroutineScope()
  val scrollBehavior = LocalTopAppBarState.current

  BoxWithConstraints(
    modifier = modifier
  ) {
    val screenHeight = maxHeight
    val scrollState = rememberScrollState()

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(
          scrollState
        )
    ) {
      TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
          Text(text = "Home")
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
          scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier,
        windowInsets = WindowInsets.displayCutout
      )
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .height(screenHeight)
      ) {
        val backgroundColor = MaterialTheme.colorScheme.background
        HomeTabs(
          selectedChartIndex = pagerState.currentPage,
          onTabTap = { tabType ->
            coroutineScope.launch {
              pagerState.animateScrollToPage(tabType.ordinal)
            }
          },
          modifier = Modifier
            .drawBehind {
              drawRect(color = backgroundColor)
            }
        )
        HorizontalPager(
          state = pagerState,
          key = {
            val homeTabType = HomeTabType.findByIndex(it)
            homeTabType.tabName
          }
        ) { page ->
          val homeTabType = HomeTabType.findByIndex(page)
          when (homeTabType) {
            HomeTabType.SCROBBLE -> {
              ScrobbleScreen(
                viewModel = hiltViewModel(),
                topAppBarScrollBehavior = scrollBehavior,
                navigateToTrackDetail = navigateToTrackDetail
              )
            }

            HomeTabType.ARTIST -> {
              TopArtistsScreen(
                viewModel = hiltViewModel(),
                onArtistTap = navigateToArtistDetail,
                topAppBarScrollBehavior = scrollBehavior
              )
            }

            HomeTabType.ALBUM -> {
              TopAlbumsScreen(
                viewModel = hiltViewModel(),
                navigateToAlbumInfo = navigateToAlbumDetail,
                topAppBarScrollBehavior = scrollBehavior
              )
            }
          }
        }
      }
    }
  }
}
