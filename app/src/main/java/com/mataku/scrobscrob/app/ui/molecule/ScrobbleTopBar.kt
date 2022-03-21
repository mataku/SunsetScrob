package com.mataku.scrobscrob.app.ui.molecule

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mataku.scrobscrob.R

@Composable
fun ScrobbleTopBar(navController: NavController) {
    var showMenu by remember {
        mutableStateOf(false)
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
                        navController.navigate("logout")
                    }
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.logout
                        )
                    )
                }

                DropdownMenuItem(
                    onClick = {}
                ) {
                    Text(text = stringResource(id = R.string.enable_notification_access))
                }
            }
        }
    )
}