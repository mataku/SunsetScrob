package com.mataku.scrobscrob.account.permission

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.core.app.NotificationManagerCompat
import com.mataku.scrobscrob.account.generated.resources.Res
import com.mataku.scrobscrob.account.generated.resources.label_notification_permission_help
import dev.zacsweers.metro.Inject
import org.jetbrains.compose.resources.stringResource

@Inject
class AndroidNotificationListenerPermission(
  private val context: Context,
) : NotificationListenerPermission {

  override fun isGranted(): Boolean =
    NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)

  @Composable
  override fun rememberRequest(onResult: (granted: Boolean) -> Unit): () -> Unit {
    val currentOnResult by rememberUpdatedState(onResult)
    val launcher = rememberLauncherForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ) {
      currentOnResult(isGranted())
    }
    val helpMessage = stringResource(Res.string.label_notification_permission_help)
    return remember(launcher, helpMessage) {
      {
        launcher.launch(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        Toast.makeText(context, helpMessage, Toast.LENGTH_LONG).show()
      }
    }
  }
}
