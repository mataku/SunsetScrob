package com.mataku.scrobscrob.app.ui.state

import android.content.Context
import androidx.navigation.NavController
import com.mataku.scrobscrob.app.ui.viewmodel.TopAlbumsViewModel
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class TopAlbumsScreenStateSpec : DescribeSpec({
    val navController = mockk<NavController>()
    val viewModel = mockk<TopAlbumsViewModel>()
    val context = mockk<Context>()

    beforeContainer {
        every {
            viewModel.uiState
        }.returns(
            TopAlbumsViewModel.UiState(
                isLoading = false,
                topAlbums = emptyList(),
                hasNext = false
            )
        )
    }

    describe("#onTapAlbum") {
        val stateHolder = TopAlbumsScreenState(
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
            stateHolder.onTapAlbum(url)
            verify(exactly = 1) {
                navController.navigate(
                    "webview?url=$url",
                    captureLambda()
                )
            }
        }
    }

    describe("#onScrollEnd") {
        val stateHolder = TopAlbumsScreenState(
            navController, viewModel, context
        )
        coEvery {
            viewModel.fetchAlbums()
        }.returns(Unit)
        it("should call fetchAlbums") {
            stateHolder.onScrollEnd()
            coVerify(exactly = 1) {
                viewModel.fetchAlbums()
            }
        }
    }
})