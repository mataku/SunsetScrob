package com.mataku.scrobscrob.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.album.ui.navigation.navigateToAlbumInfo
import com.mataku.scrobscrob.album.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.artist.ui.navigation.navigateToArtistInfo
import com.mataku.scrobscrob.artist.ui.screen.TopArtistsScreen
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.home.HomeTabType
import com.mataku.scrobscrob.home.ui.molecule.HomeTabs
import com.mataku.scrobscrob.scrobble.ui.navigation.navigateToTrackDetail
import com.mataku.scrobscrob.scrobble.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.LocalTopAppBarState
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
  val scrollBehavior = LocalTopAppBarState.current

  BoxWithConstraints {
    val screenHeight = maxHeight
    val scrollState = rememberScrollState()

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(
          scrollState
        )
    ) {
      ContentHeader(text = "Home")
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .height(screenHeight)
      ) {

        HomeTabs(
          selectedChartIndex = pagerState.currentPage,
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
                topAppBarScrollBehavior = scrollBehavior,
                navigateToTrackDetail = { track ->
                  navController.navigateToTrackDetail(
                    trackName = track.name,
                    artistName = track.artistName,
                    imageUrl = track.images.imageUrl() ?: "",
                  )
                }
              )
            }

            HomeTabType.ARTIST -> {
              TopArtistsScreen(
                viewModel = hiltViewModel(),
                onArtistTap = {
                  navController.navigateToArtistInfo(
                    artistName = it.name,
                    artworkUrl = it.imageList.imageUrl() ?: "",
                  )
                },
                topAppBarScrollBehavior = scrollBehavior
              )
            }

            HomeTabType.TRACK -> {
              TopAlbumsScreen(
                viewModel = hiltViewModel(),
                navigateToAlbumInfo = { album ->
                  navController.navigateToAlbumInfo(
                    albumName = album.title,
                    artistName = album.artist,
                    artworkUrl = album.imageList.imageUrl() ?: ""
                  )
                },
                topAppBarScrollBehavior = scrollBehavior
              )
            }
          }
        }
      }
    }
  }
}
