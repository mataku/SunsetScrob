package com.mataku.scrobscrob.auth.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.mataku.scrobscrob.auth.ui.viewmodel.LogoutConfirmationViewModel

class LogoutConfirmationDialogState(
    private val navController: NavHostController,
    private val viewModel: LogoutConfirmationViewModel
) {
    val uiState = viewModel.uiState

    fun navigateToLoginScreen() {
        navController.navigate("login") {
            launchSingleTop = true
            popUpTo(
                navController.graph.findStartDestination().id
            ) {
                inclusive = true
            }
        }
    }

    fun logout() {
        viewModel.logout()
    }
}

@Composable
fun rememberLogoutConfirmationDialogState(
    navController: NavHostController,
    LogoutConfirmationViewModel: LogoutConfirmationViewModel = hiltViewModel()
): LogoutConfirmationDialogState =
    remember {
        LogoutConfirmationDialogState(
            navController = navController,
            viewModel = LogoutConfirmationViewModel
        )
    }