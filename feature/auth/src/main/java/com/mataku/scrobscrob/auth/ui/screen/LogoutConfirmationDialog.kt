package com.mataku.scrobscrob.auth.ui.screen

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mataku.scrobscrob.auth.ui.state.LogoutConfirmationDialogState

@Composable
fun LogoutConfirmationDialog(
    stateHolder: LogoutConfirmationDialogState
) {
    val openStatus = remember {
        mutableStateOf(true)
    }
    val uiState = stateHolder.uiState
    if (uiState.logoutEvent != null) {
        openStatus.value = false
        stateHolder.navigateToLoginScreen()
    }
    if (openStatus.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = "Logout?")
            },
            confirmButton = {
                TextButton(onClick = {
                    stateHolder.logout()
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