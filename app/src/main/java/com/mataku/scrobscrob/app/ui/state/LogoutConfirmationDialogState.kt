package com.mataku.scrobscrob.app.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.app.ui.viewmodel.LogoutViewModel

class LogoutConfirmationDialogState(
    val navController: NavController,
    val viewModel: LogoutViewModel
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
    navController: NavController = rememberNavController(),
    logoutViewModel: LogoutViewModel = hiltViewModel()
): LogoutConfirmationDialogState =
    remember {
        LogoutConfirmationDialogState(
            navController = navController,
            viewModel = logoutViewModel
        )
    }