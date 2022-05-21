package com.mataku.scrobscrob.app.ui.state

import androidx.navigation.NavController
import com.mataku.scrobscrob.app.ui.molecule.SunsetBottomNavItem
import com.mataku.scrobscrob.app.ui.viewmodel.LoginViewModel
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class LoginScreenStateSpec : DescribeSpec({
    val navController = mockk<NavController>()
    val viewModel = mockk<LoginViewModel>()
    beforeContainer {
        every {
            viewModel.uiState
        }.returns(
            LoginViewModel.UiState(
                isLoading = false
            )
        )
    }

    val username = "username"
    val password = "password"

    describe("#login") {
        val loginScreenState = LoginScreenState(
            navController = navController,
            viewModel = viewModel
        )
        it("calls authorize") {
            coEvery {
                viewModel.authorize(username = username, password = password)
            }.returns(Unit)
            loginScreenState.login(username, password)
            coVerify(exactly = 1) {
                viewModel.authorize(username = username, password = password)
            }
        }
    }

    describe("#navigateToTop") {
        val loginScreenState = LoginScreenState(
            navController = navController,
            viewModel = viewModel
        )
        it("calls NavController.navigate") {
            every {
                navController.navigate(
                    SunsetBottomNavItem.SCROBBLE.screenRoute,
                    captureLambda()
                )
            }.answers { }
            loginScreenState.navigateToTop()
            verify(exactly = 1) {
                navController.navigate(
                    SunsetBottomNavItem.SCROBBLE.screenRoute,
                    captureLambda()
                )
            }
        }
    }
})