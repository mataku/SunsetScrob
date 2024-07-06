package com.mataku.scrobscrob.app.ui.top

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.mataku.scrobscrob.app.ui.screen.MainScreen
import com.mataku.scrobscrob.app.ui.viewmodel.MainViewModel
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    // Should Call before onCreate
    // https://developer.android.com/guide/topics/ui/splash-screen/migrate#migrate_your_splash_screen_implementation
    installSplashScreen()

    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    lifecycleScope.launch {
      viewModel.state.collect {
        it?.let { uiState ->
          val theme = uiState.theme
          enableEdgeToEdge(
            statusBarStyle = if (theme.isLight) {
              SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
              )
            } else {
              SystemBarStyle.dark(
                Color.Transparent.toArgb(),
              )
            }
          )
          setContent {
            SunsetTheme(theme = theme) {
              MainScreen(
                username = uiState.username,
              )
            }
          }
        }
      }
    }
  }
}
