package com.mataku.scrobscrob.account.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel

class AccountState(
    private val navController: NavController,
    private val viewModel: AccountViewModel
) {
    val uiState: AccountViewModel.UiState
        @Composable get() = viewModel.uiState.collectAsState().value

    fun navigateToThemeSelector() {
        navController.navigate("theme")
    }
}

@Composable
fun rememberAccountState(
    navController: NavController,
    viewModel: AccountViewModel = hiltViewModel()
): AccountState {
    return remember {
        AccountState(viewModel = viewModel, navController = navController)
    }
}