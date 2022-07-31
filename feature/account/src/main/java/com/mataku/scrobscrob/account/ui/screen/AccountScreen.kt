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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.account.AccountMenu
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.state.AccountState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.SunsetTheme

@Composable
fun AccountScreen(
    state: AccountState
) {
    val uiState = state.uiState
    uiState.theme?.let {
        AccountContent(
            theme = it,
            navigateToThemeSelector = { state.navigateToThemeSelector() }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AccountContent(
    theme: AppTheme,
    navigateToThemeSelector: () -> Unit
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
            AccountContent(theme = AppTheme.DARK, navigateToThemeSelector = {})
        }
    }
}