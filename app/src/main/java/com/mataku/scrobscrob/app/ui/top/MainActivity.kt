package com.mataku.scrobscrob.app.ui.top

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mataku.scrobscrob.app.ui.screen.MainScreen
import com.mataku.scrobscrob.app.ui.viewmodel.MainViewModel
import com.mataku.scrobscrob.home.ui.navigation.HOME_NAVIGATION_ROUTE
import com.mataku.scrobscrob.ui_common.LOGIN_DESTINATION
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import kotlinx.coroutines.launch

@ContributesIntoMap(AppScope::class, binding = binding<Activity>())
@ActivityKey
@Inject
class MainActivity(
  private val viewModelFactory: MetroViewModelFactory,
) : ComponentActivity() {
  private val viewModel by viewModels<MainViewModel>()

  override val defaultViewModelProviderFactory: ViewModelProvider.Factory
    get() = viewModelFactory

  override fun onCreate(savedInstanceState: Bundle?) {
    // Should Call before onCreate
    // https://developer.android.com/guide/topics/ui/splash-screen/migrate#migrate_your_splash_screen_implementation
    installSplashScreen()

    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    lifecycleScope.launch {
      viewModel.state.collect {
        it?.let { uiState ->
          val isSystemDark =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
              Configuration.UI_MODE_NIGHT_YES
          val resolvedTheme = uiState.theme.resolve(isSystemDark)
          enableEdgeToEdge(
            statusBarStyle = if (resolvedTheme.isLight) {
              SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Colors.StatusBarDark.toArgb()
              )
            } else {
              SystemBarStyle.dark(
                Color.Transparent.toArgb(),
              )
            }
          )
          val startDestination = if (uiState.username.isNullOrEmpty()) {
            LOGIN_DESTINATION
          } else {
            HOME_NAVIGATION_ROUTE
          }
          setContent {
            CompositionLocalProvider(LocalMetroViewModelFactory provides viewModelFactory) {
              SunsetTheme(theme = uiState.theme) {
                MainScreen(startDestination = startDestination)
              }
            }
          }
        }
      }
    }
  }
}
