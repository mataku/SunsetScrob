package com.mataku.scrobscrob.artist.ui.state

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.navigation.NavHostController
import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.MutableStateFlow

class TopArtistsScreenStateSpec : DescribeSpec({
  val navController = mockk<NavHostController>()
  val viewModel = mockk<TopArtistsViewModel>()
  val context = mockk<Context>()
  val resources = mockk<Resources>()
  val displayMetrics = mockk<DisplayMetrics>(relaxed = true)

  beforeContainer {
    every {
      viewModel.uiState
    }.returns(
      MutableStateFlow(
        TopArtistsViewModel.TopArtistsUiState(
          isLoading = false,
          topArtists = emptyList<ArtistInfo>().toImmutableSet(),
          hasNext = false,
          selectedTimeRangeFiltering = TimeRangeFiltering.OVERALL
        )
      )
    )
    every {
      context.resources
    }.returns(resources)
    every {
      resources.displayMetrics
    }.returns(displayMetrics)

//        every {
//            displayMetrics.getProperty("widthPixels")
//        }.returns(100)
//
//        every {
//            displayMetrics.getProperty("density")
//        }.returns(1F)
  }

  describe("#onTapArtist") {
    val stateHolder = TopArtistsScreenState(
      navController, viewModel, context
    )
    val url = "https://example.com"
    it("should navigate to webView") {
      every {
        navController.navigate(
          "webview?url=$url"
        )
      }.answers { }
      stateHolder.onTapArtist(url)
      verify(exactly = 1) {
        navController.navigate(
          "webview?url=$url"
        )
      }
    }
  }

  describe("#onScrollEnd") {
    val stateHolder = TopArtistsScreenState(
      navController, viewModel, context
    )
    coEvery {
      viewModel.fetchTopArtists()
    }.returns(Unit)
    it("should call fetchAlbums") {
      stateHolder.onScrollEnd()
      coVerify(exactly = 1) {
        viewModel.fetchTopArtists()
      }
    }
  }
})
