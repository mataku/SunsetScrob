package com.mataku.scrobscrob.account.update

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

interface InAppUpdateManager {
  fun updateStatus(): Flow<AppUpdateStatus>

  suspend fun completeUpdate()

  @Composable
  fun rememberStartUpdate(): () -> Unit
}
