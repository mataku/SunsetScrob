package com.mataku.scrobscrob.scrobble.ui.molecule

import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.mataku.scrobscrob.scrobble.ui.state.ScrobbleTopBarState
import com.mataku.scrobscrob.ui_common.R

@Composable
fun ScrobbleTopBar(stateHolder: ScrobbleTopBarState) {
    var showMenu by remember {
        mutableStateOf(false)
    }

    var dialogShow by remember {
        mutableStateOf(false)
    }

    val uiState = stateHolder.uiState
    uiState.logoutEvent?.let {
        stateHolder.navigateToLoginScreen()
    }

    TopAppBar(
        title = {
            Text("Scrobbles")
        },
        actions = {
            IconButton(onClick = {
                showMenu = !showMenu
            }) {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = "settings")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        showMenu = !showMenu
                        dialogShow = true
                    }
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.logout
                        )
                    )
                }

                DropdownMenuItem(
                    onClick = {
                        stateHolder.navigateToNotificationListenerSettings()
                    }
                ) {
                    Text(text = stringResource(id = R.string.enable_notification_access))
                }
            }
        }
    )

    if (dialogShow) {
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
                    dialogShow = false
                }) {
                    Text(text = "NO")
                }
            }
        )
    }
}