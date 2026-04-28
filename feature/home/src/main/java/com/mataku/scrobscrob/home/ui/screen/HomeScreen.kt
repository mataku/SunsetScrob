package com.mataku.scrobscrob.home.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.mataku.scrobscrob.scrobble.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar
import com.mataku.scrobscrob.ui_common.component.designsystem.rememberSunsetTopAppBarScrollBehavior
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun HomeScreen(
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
    val scrollBehavior = rememberSunsetTopAppBarScrollBehavior()

    SunsetScaffold(
      modifier = modifier,
      topBar = {
        SunsetTopAppBar(
          title = {
            SunsetText.Title(text = "Home")
          },
          scrollBehavior = scrollBehavior,
        )
      }
    ) { paddingValues ->
      BoxWithConstraints(
        modifier = Modifier.padding(paddingValues)
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
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .height(screenHeight)
          ) {
            val backgroundColor = LocalAppTheme.current.backgroundColor()
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
    }
  }
}
