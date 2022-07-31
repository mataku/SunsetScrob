package com.mataku.scrobscrob.account.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class AccountState()

@Composable
fun rememberAccountState(): AccountState {
    return remember {
        AccountState()
    }
}