package com.mataku.scrobscrob.account.permission

import androidx.compose.runtime.Composable

interface NotificationListenerPermission {
  fun isGranted(): Boolean

  @Composable
  fun rememberRequest(onResult: (granted: Boolean) -> Unit): () -> Unit
}
