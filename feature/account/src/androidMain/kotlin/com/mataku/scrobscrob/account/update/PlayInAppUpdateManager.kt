package com.mataku.scrobscrob.account.update

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.requestAppUpdateInfo
import com.google.android.play.core.ktx.requestCompleteUpdate
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

@Inject
@SingleIn(AppScope::class)
class PlayInAppUpdateManager(
  private val appUpdateManager: AppUpdateManager,
) : InAppUpdateManager {

  override fun updateStatus(): Flow<AppUpdateStatus> = callbackFlow {
    val listener = InstallStateUpdatedListener { state ->
      if (state.installStatus() == InstallStatus.DOWNLOADED) {
        trySend(AppUpdateStatus.DOWNLOADED)
      }
    }
    appUpdateManager.registerListener(listener)
    runCatching { appUpdateManager.requestAppUpdateInfo() }
      .onSuccess { trySend(it.toStatus()) }
      .onFailure { close(it) }
    awaitClose { appUpdateManager.unregisterListener(listener) }
  }

  override suspend fun completeUpdate() {
    appUpdateManager.requestCompleteUpdate()
  }

  @Composable
  override fun rememberStartUpdate(): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
      ActivityResultContracts.StartIntentSenderForResult()
    ) {}
    val coroutineScope = rememberCoroutineScope()
    return remember(launcher, coroutineScope) {
      {
        coroutineScope.launch {
          runCatching {
            val info = appUpdateManager.requestAppUpdateInfo()
            appUpdateManager.startUpdateFlowForResult(
              info,
              launcher,
              AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE),
            )
          }
        }
      }
    }
  }

  private fun AppUpdateInfo.toStatus(): AppUpdateStatus = when {
    installStatus() == InstallStatus.DOWNLOADED -> AppUpdateStatus.DOWNLOADED
    updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE -> AppUpdateStatus.AVAILABLE
    else -> AppUpdateStatus.NONE
  }
}
