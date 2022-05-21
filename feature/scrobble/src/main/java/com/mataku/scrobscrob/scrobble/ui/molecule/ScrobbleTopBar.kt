package com.mataku.scrobscrob.scrobble.ui.molecule

import android.content.Intent
import android.provider.Settings
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mataku.scrobscrob.ui_common.R

@Composable
fun ScrobbleTopBar(navController: NavController) {
    var showMenu by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

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
                    onClick = {
                        val intent = Intent()
                        intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
                        context.startActivity(intent)
                    }
                ) {
                    Text(text = stringResource(id = R.string.enable_notification_access))
                }
            }
        }
    )
}