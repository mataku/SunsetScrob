package com.mataku.scrobscrob.auth.ui.state

import androidx.navigation.NavHostController
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow

class LoginScreenStateSpec : DescribeSpec({
  val navController = mockk<NavHostController>()
  val viewModel = mockk<LoginViewModel>()
  beforeContainer {
    every {
      viewModel.uiState
    }.returns(
      MutableStateFlow(
        LoginViewModel.UiState(
          isLoading = false,
          username = "",
          password = ""
        )
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
