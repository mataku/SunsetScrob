package com.mataku.scrobscrob.home.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.album.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.artist.ui.screen.TopArtistsScreen
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.home.HomeTabType
import com.mataku.scrobscrob.home.ui.molecule.HomeTabs
import com.mataku.scrobscrob.home.ui.viewmodel.HomeViewModel
import com.mataku.scrobscrob.scrobble.ui.navigation.TrackDetailKey
import com.mataku.scrobscrob.scrobble.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar
import com.mataku.scrobscrob.ui_common.component.designsystem.rememberSunsetTopAppBarScrollBehavior
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.launch

@Composable
internal fun HomeScreen(
  viewModel: HomeViewModel,
  sharedTransitionScope: SharedTransitionScope,
  animatedContentScope: AnimatedContentScope,
  navigateToTrackDetail: (RecentTrack, String) -> Unit,
  trackViewModelProvider: @Composable (TrackDetailKey) -> TrackViewModel,
  navigateToWebView: (String) -> Unit,
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
    val scrollBehavior = rememberSunsetTopAppBarScrollBehavior()
    val backgroundColor = LocalAppTheme.current.backgroundColor()

    SunsetScaffold(
      modifier = modifier,
      topBar = {
        Column(
          modifier = Modifier.drawBehind {
            drawRect(color = backgroundColor)
          }
        ) {
          SunsetTopAppBar(
            title = {
              SunsetText.Title(text = "Home")
            },
            scrollBehavior = scrollBehavior,
          )
          HomeTabs(
            selectedChartIndex = pagerState.currentPage,
            onTabTap = { tabType ->
              coroutineScope.launch {
                pagerState.animateScrollToPage(tabType.ordinal)
              }
            }
          )
        }
      }
    ) { paddingValues ->
      HorizontalPager(
        state = pagerState,
        key = {
          val homeTabType = HomeTabType.findByIndex(it)
          homeTabType.tabName
        },
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
      ) { page ->
        val homeTabType = HomeTabType.findByIndex(page)
        when (homeTabType) {
          HomeTabType.SCROBBLE -> {
            ScrobbleScreen(
              topAppBarScrollBehavior = scrollBehavior,
              navigateToTrackDetail = navigateToTrackDetail,
              trackViewModelProvider = trackViewModelProvider,
              navigateToWebView = navigateToWebView,
              sharedTransitionScope = sharedTransitionScope,
              animatedContentScope = animatedContentScope,
              viewModel = metroViewModel(key = "scrobble"),
            )
          }

          HomeTabType.ARTIST -> {
            TopArtistsScreen(
              viewModel = metroViewModel(key = "artist"),
              onArtistTap = navigateToArtistDetail,
              topAppBarScrollBehavior = scrollBehavior,
              sharedTransitionScope = sharedTransitionScope,
              animatedContentScope = animatedContentScope
            )
          }

          HomeTabType.ALBUM -> {
            TopAlbumsScreen(
              viewModel = metroViewModel(key = "album"),
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
