package com.mataku.scrobscrob.auth.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem

class LoginScreenState(
    private val navController: NavHostController,
    private val viewModel: LoginViewModel
) {
    val uiState: LoginViewModel.UiState
        @Composable get() = viewModel.uiState.collectAsState().value

    fun login(username: String, password: String) {
        viewModel.authorize(
            username = username,
            password = password
        )
    }

    fun navigateToTop() {
        navController.navigate(
            SunsetBottomNavItem.SCROBBLE.screenRoute
        ) {
            launchSingleTop = true
            popUpTo(
                navController.graph.findStartDestination().id
            ) {
                inclusive = true
            }
        }
    }

    fun popEvent() {
        viewModel.popEvent()
    }

    sealed class UiEvent {
        object LoginSuccess : UiEvent()
        object LoginFailed : UiEvent()
    }
}

@Composable
fun rememberLoginScreenState(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
): LoginScreenState =
    remember(navController, viewModel) {
        LoginScreenState(
            navController = navController,
            viewModel = viewModel
        )
    }