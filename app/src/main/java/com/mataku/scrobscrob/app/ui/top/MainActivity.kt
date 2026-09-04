package com.mataku.scrobscrob.app.ui.top

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.account.permission.NotificationListenerPermission
import com.mataku.scrobscrob.account.update.InAppUpdateManager
import com.mataku.scrobscrob.app.ui.screen.SunsetMainScreen
import com.mataku.scrobscrob.app.ui.viewmodel.MainViewModel
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuth
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthLauncher
import com.mataku.scrobscrob.auth.webauth.WebAuthCallbackChannel
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

@ContributesIntoMap(AppScope::class, binding = binding<Activity>())
@ActivityKey
@Inject
class MainActivity(
  private val viewModelFactory: MetroViewModelFactory,
  private val webAuthLauncher: LastFmWebAuthLauncher,
  private val webAuthCallback: WebAuthCallbackChannel,
  private val inAppUpdateManager: InAppUpdateManager,
  private val notificationListenerPermission: NotificationListenerPermission,
) : ComponentActivity() {
  private val viewModel by viewModels<MainViewModel>()

  override val defaultViewModelProviderFactory: ViewModelProvider.Factory
    get() = viewModelFactory

  override fun onCreate(savedInstanceState: Bundle?) {
    val splashScreen = installSplashScreen()
    splashScreen.setKeepOnScreenCondition { viewModel.state.value == null }

    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      handleWebAuthCallback(intent)
    }

    setContent {
      val uiState by viewModel.state.collectAsStateWithLifecycle()
      val state = uiState ?: return@setContent

      val isSystemDark =
        (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
          Configuration.UI_MODE_NIGHT_YES
      val resolvedTheme = state.theme.resolve(isSystemDark)

      LaunchedEffect(resolvedTheme) {
        enableEdgeToEdge(
          statusBarStyle = if (resolvedTheme.isLight) {
            SystemBarStyle.light(
              Color.Transparent.toArgb(),
              Colors.StatusBarDark.toArgb(),
            )
          } else {
            SystemBarStyle.dark(Color.Transparent.toArgb())
          },
        )
      }

      val isAuthenticated = !state.username.isNullOrEmpty()

      CompositionLocalProvider(LocalMetroViewModelFactory provides viewModelFactory) {
        SunsetTheme(theme = state.theme) {
          SunsetMainScreen(
            isAuthenticated = isAuthenticated,
            webAuthLauncher = webAuthLauncher,
            inAppUpdateManager = inAppUpdateManager,
            notificationListenerPermission = notificationListenerPermission,
          )
        }
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    handleWebAuthCallback(intent)
  }

  private fun handleWebAuthCallback(intent: Intent?) {
    val token = intent?.dataString?.let(LastFmWebAuth::tokenFromCallback) ?: return
    webAuthCallback.offer(token)
  }
}
