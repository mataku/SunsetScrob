package com.mataku.scrobscrob.app.ui.molecule

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.mataku.scrobscrob.app.ui.viewmodel.LogoutViewModel

@Composable
fun LogoutDialog(
    navController: NavController,
    viewModel: LogoutViewModel
) {
    val openStatus = remember {
        mutableStateOf(true)
    }
    val uiState = viewModel.uiState
    if (uiState.logoutEvent != null) {
        openStatus.value = false
        navController.navigate(SunsetBottomNavItem.SCROBBLE.screenRoute)
    }
    if (openStatus.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = "Logout?")
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.logout()
                }) {
                    Text(text = "Let me out!")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openStatus.value = false
                }) {
                    Text(text = "NO")
                }

            }
        )
    }
}