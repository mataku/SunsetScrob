package com.mataku.scrobscrob.account.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import com.mataku.scrobscrob.core.entity.AppTheme

class ThemeSelectorState(
    private val viewModel: ThemeSelectorViewModel
) {
    val uiState: ThemeSelectorViewModel.UiState
        @Composable get() = viewModel.uiState.collectAsState().value

    fun changeTheme(theme: AppTheme) {
        viewModel.changeTheme(theme)
    }
}

@Composable
fun rememberThemeSelectorState(
    navController: NavController,
    viewModel: ThemeSelectorViewModel = hiltViewModel()
): ThemeSelectorState {
    return remember {
        ThemeSelectorState(
            viewModel = viewModel
        )
    }
}