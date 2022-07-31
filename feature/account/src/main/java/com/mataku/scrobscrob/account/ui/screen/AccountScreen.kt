package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mataku.scrobscrob.account.ui.state.AccountState

@Composable
fun AccountScreen(
    state: AccountState
) {
    AccountContent()
}

@Composable
private fun AccountContent() {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}