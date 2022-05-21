package com.mataku.scrobscrob.app.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.app.ui.molecule.SunsetBottomNavItem
import com.mataku.scrobscrob.app.ui.viewmodel.LoginViewModel

class LoginScreenState(
    private val navController: NavController,
    private val viewModel: LoginViewModel
) {
    val uiState = viewModel.uiState

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
}

@Composable
fun rememberLoginScreenState(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
): LoginScreenState =
    remember {
        LoginScreenState(
            navController = navController,
            viewModel = viewModel
        )
    }