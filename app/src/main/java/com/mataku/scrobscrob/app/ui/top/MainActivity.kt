package com.mataku.scrobscrob.app.ui.top

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.mataku.scrobscrob.app.ui.screen.MainScreen
import com.mataku.scrobscrob.app.ui.viewmodel.MainViewModel
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    // Should Call before onCreate
    // https://developer.android.com/guide/topics/ui/splash-screen/migrate#migrate_your_splash_screen_implementation
    installSplashScreen()
    super.onCreate(savedInstanceState)

    lifecycleScope.launch {
      viewModel.state.collect {
        it?.let { statePair ->
          setContent {
            SunsetTheme(theme = statePair.first) {
              MainScreen(username = statePair.second)
            }
          }
        }
      }
    }
  }
}
