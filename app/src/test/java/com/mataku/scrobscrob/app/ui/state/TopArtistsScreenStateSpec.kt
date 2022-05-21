package com.mataku.scrobscrob.app.ui.state

import android.content.Context
import androidx.navigation.NavController
import com.mataku.scrobscrob.app.ui.viewmodel.TopArtistsViewModel
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class TopArtistsScreenStateSpec : DescribeSpec({
    val navController = mockk<NavController>()
    val viewModel = mockk<TopArtistsViewModel>()
    val context = mockk<Context>()

    beforeContainer {
        every {
            viewModel.uiState
        }.returns(
            TopArtistsViewModel.UiState(
                isLoading = false,
                topArtists = emptyList(),
                hasNext = false
            )
        )
    }

    describe("#onTapArtist") {
        val stateHolder = TopArtistsScreenState(
            navController, viewModel, context
        )
        val url = "https://example.com"
        it("should navigate to webView") {
            every {
                navController.navigate(
                    "webview?url=$url",
                    captureLambda()
                )
            }.answers { }
            stateHolder.onTapArtist(url)
            verify(exactly = 1) {
                navController.navigate(
                    "webview?url=$url",
                    captureLambda()
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