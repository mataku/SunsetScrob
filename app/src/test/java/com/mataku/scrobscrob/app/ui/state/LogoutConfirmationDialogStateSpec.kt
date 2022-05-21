package com.mataku.scrobscrob.app.ui.state

import androidx.navigation.NavController
import com.mataku.scrobscrob.app.ui.viewmodel.LogoutViewModel
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class LogoutConfirmationDialogStateSpec : DescribeSpec({
    val navController = mockk<NavController>()
    val viewModel = mockk<LogoutViewModel>()

    beforeContainer {
        every {
            viewModel.uiState
        }.returns(
            LogoutViewModel.UiState()
        )
    }

    describe("#logout") {
        val stateHolder = LogoutConfirmationDialogState(
            navController, viewModel
        )
        it("calls logout of ViewModel") {
            coEvery {
                viewModel.logout()
            }.returns(Unit)
            stateHolder.logout()
            coVerify(exactly = 1) {
                viewModel.logout()
            }
        }
    }

    describe("#navigateToLoginScreen") {
        val stateHolder = LogoutConfirmationDialogState(
            navController, viewModel
        )
        it("calls navigate of NavController") {
            every {
                navController.navigate(
                    "login",
                    captureLambda()
                )
            }.answers { }
            stateHolder.navigateToLoginScreen()
            verify(exactly = 1) {
                navController.navigate(
                    "login",
                    captureLambda()
                )
            }
        }
    }

})