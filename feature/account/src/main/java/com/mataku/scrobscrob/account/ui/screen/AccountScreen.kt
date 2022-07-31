package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.account.AccountMenu
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.state.AccountState
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.SunsetTheme

@Composable
fun AccountScreen(
    state: AccountState
) {
    val openDialog = remember {
        mutableStateOf(false)
    }
    val uiState = state.uiState
    uiState.theme?.let {
        AccountContent(
            theme = it,
            navigateToThemeSelector = { state.navigateToThemeSelector() },
            navigateToLogoutConfirmation = {
                openDialog.value = true
            }
        )
    }

    uiState.event?.let {
        when (it) {
            is AccountViewModel.Event.Logout -> {
                state.navigateToLoginScreen()
            }
        }
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Logout?")
            },
            confirmButton = {
                TextButton(onClick = {
                    state.logout()
                }) {
                    Text(
                        text = "Let me out!", style = SunsetTextStyle.body1.copy(
                            MaterialTheme.colors.onSurface
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog.value = false
                }) {
                    Text(
                        text = "NO", style = SunsetTextStyle.body1.copy(
                            MaterialTheme.colors.onSurface
                        )
                    )
                }
            }
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AccountContent(
    theme: AppTheme,
    navigateToThemeSelector: () -> Unit,
    navigateToLogoutConfirmation: () -> Unit
) {
    LazyColumn(
        content = {
            stickyHeader {
                ContentHeader(text = stringResource(id = R.string.menu_account))
            }
            item {
                val menu = AccountMenu.THEME
                AccountMenuCell(
                    title = stringResource(id = menu.titleRes),
                    description = theme.rawValue
                ) {
                    navigateToThemeSelector.invoke()
                }
                val logoutMenu = AccountMenu.LOGOUT
                AccountMenuCell(
                    title = stringResource(id = logoutMenu.titleRes),
                    description = stringResource(id = logoutMenu.descriptionRes)
                ) {
                    navigateToLogoutConfirmation.invoke()
                }
            }

        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun AccountMenuCell(
    title: String,
    description: String,
    onTapAccount: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .clickable {
            onTapAccount.invoke()
        }
        .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = title, style = SunsetTextStyle.subtitle1)
        Text(text = description, style = SunsetTextStyle.caption)
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountContentPreview() {
    SunsetTheme {
        androidx.compose.material.Surface {
            AccountContent(
                theme = AppTheme.DARK,
                navigateToThemeSelector = {},
                navigateToLogoutConfirmation = {})
        }
    }
}