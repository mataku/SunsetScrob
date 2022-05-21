package com.mataku.scrobscrob.app.ui.state

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
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
    val resources = mockk<Resources>()
    val displayMetrics = mockk<DisplayMetrics>(relaxed = true)

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

    describe("#onTapAlbum") {
        val stateHolder = TopAlbumsScreenState(
            navController, viewModel, context
        )
        val url = "https://example.com"
        it("should navigate to webView") {
            every {
                navController.navigate(
                    "webview?url=$url"
                )
            }.answers { }
            stateHolder.onTapAlbum(url)
            verify(exactly = 1) {
                navController.navigate(
                    "webview?url=$url"
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