package com.mataku.scrobscrob.home.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  sharedTransitionScope: SharedTransitionScope,
  animatedContentScope: AnimatedContentScope,
  navigateToTrackDetail: (RecentTrack, String) -> Unit,
  navigateToArtistDetail: (TopArtistInfo, String) -> Unit,
  navigateToAlbumDetail: (TopAlbumInfo, String) -> Unit,
  navigateToLogin: () -> Unit,
  modifier: Modifier = Modifier
) {

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(uiState.events) {
    uiState.events.firstOrNull()?.let { event ->
      when (event) {
        is HomeViewModel.HomeUiEvent.RedirectToLogin -> {
          navigateToLogin.invoke()
        }
      }

      viewModel.consumeEvent(event)
    }
  }

  if (uiState.username.isNotEmpty()) {
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
//        TopAppBar(
//          scrollBehavior = scrollBehavior,
//          title = {
//            Text(
//              text = "Home",
//              style = SunsetTextStyle.title,
//            )
//          },
//          colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.background,
//            scrolledContainerColor = MaterialTheme.colorScheme.background
//          ),
//          modifier = Modifier,
//          windowInsets = WindowInsets.displayCutout
//        )
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
                  topAppBarScrollBehavior = scrollBehavior,
                  navigateToTrackDetail = navigateToTrackDetail,
                  sharedTransitionScope = sharedTransitionScope,
                  animatedContentScope = animatedContentScope,
                  viewModel = hiltViewModel(key = "scrobble"),
                )
              }

              HomeTabType.ARTIST -> {
                TopArtistsScreen(
                  viewModel = hiltViewModel(key = "artist"),
                  onArtistTap = navigateToArtistDetail,
                  topAppBarScrollBehavior = scrollBehavior,
                  sharedTransitionScope = sharedTransitionScope,
                  animatedContentScope = animatedContentScope
                )
              }

              HomeTabType.ALBUM -> {
                TopAlbumsScreen(
                  viewModel = hiltViewModel(key = "album"),
                  navigateToAlbumInfo = navigateToAlbumDetail,
                  topAppBarScrollBehavior = scrollBehavior,
                  sharedTransitionScope = sharedTransitionScope,
                  animatedContentScope = animatedContentScope
                )
              }
            }
          }
        }
      }
    }
  }
}
