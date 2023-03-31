package com.mataku.scrobscrob.ui_common.style

import androidx.compose.material.ScaffoldState
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.core.entity.AppTheme

@Composable
fun SunsetTheme(
  theme: AppTheme = AppTheme.DARK,
  content: @Composable () -> Unit,
) {
  val systemUiController = rememberSystemUiController()
  systemUiController.setSystemBarsColor(
    theme.backgroundColor()
  )
  systemUiController.setNavigationBarColor(
    theme.colorScheme().primary
  )
  val rippleTheme = SunsetRippleTheme(theme)
  MaterialTheme(
    colorScheme = theme.colorScheme(),
  ) {
    CompositionLocalProvider(
      LocalRippleTheme provides rippleTheme,
      LocalAppTheme provides theme,
      content = content
    )
  }
}

@Composable
fun SunsetThemePreview(
  theme: AppTheme = AppTheme.DARK,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = theme.colorScheme(),
  ) {
    CompositionLocalProvider(
      LocalAppTheme provides theme,
      content = content
    )
  }
}

val LocalAppTheme = staticCompositionLocalOf { AppTheme.DARK }
val LocalScaffoldState = staticCompositionLocalOf<ScaffoldState> {
  throw IllegalStateException("LocalScaffoldState provider is required")
}

const val ANIMATION_DURATION_MILLIS = 700
const val TRANSITION_ANIMATION_DURATION_MILLIS = 600

fun AppTheme.backgroundColor(): Color {
  return this.colorScheme().background
}

fun AppTheme.accentColor(): Color {
  return when (this) {
    AppTheme.DARK, AppTheme.LIGHT, AppTheme.MIDNIGHT, AppTheme.SUNSET -> {
      Colors.LightLime
    }
    AppTheme.OCEAN -> {
      Colors.DeepOcean
    }
    AppTheme.LASTFM_DARK -> {
      Colors.LastFmColor
    }
  }
}

fun AppTheme.colorScheme(): ColorScheme {
  return when (this) {
    AppTheme.DARK -> {
      darkColorScheme
    }
    AppTheme.LIGHT -> {
      lightColorScheme
    }
    AppTheme.MIDNIGHT -> {
      midnightColorScheme
    }
    AppTheme.OCEAN -> {
      oceanColorScheme
    }
    AppTheme.LASTFM_DARK -> {
      lastFmDarkColorScheme
    }
    AppTheme.SUNSET -> {
      sunsetColorScheme
    }
  }
}

private val lightColorScheme = lightColorScheme(
  primary = Color(0xFFE0E0E0),
  onPrimary = Color.Black,
  secondary = Color.White,
  onSecondary = Colors.textSecondary,
  surface = Color(0xFFF5F5F5),
  onSurface = Color.Black,
  background = Color(0xFFF5F5F5),
  onBackground = Color.Black,
)

private val darkColorScheme = darkColorScheme(
  primary = Color.DarkGray,
  onPrimary = Color.White,
  secondary = Color.White,
  onSecondary = Colors.textSecondary,
  surface = Color(0xFF37474F),
  onSurface = Color.White,
  background = Colors.ContentBackground,
  onBackground = Color.White
)

private val oceanColorScheme = darkColorScheme(
  primary = Color.DarkGray,
  onPrimary = Color.White,
  secondary = Color.White,
  onSecondary = Colors.textSecondary,
  surface = Color(0xFF37474F),
  onSurface = Color.White,
  background = Colors.DeepOceanBackground,
  onBackground = Color.White
)

private val lastFmDarkColorScheme = darkColorScheme(
  primary = Color.DarkGray,
  onPrimary = Color.White,
  secondary = Color.White,
  onSecondary = Colors.textSecondary,
  surface = Color(0xFF37474F),
  onSurface = Color.White,
  background = Colors.ContentBackground,
  onBackground = Color.White
)

private val midnightColorScheme = darkColorScheme(
  primary = Color.DarkGray,
  onPrimary = Color.White,
  secondary = Color.White,
  onSecondary = Colors.textSecondary,
  surface = Color.Black,
  onSurface = Color.White,
  background = Color.Black,
  onBackground = Color.White
)

private val sunsetColorScheme = lightColorScheme(
  primary = Color(0xFFE0E0E0),
  onPrimary = Color.Black,
  secondary = Colors.SunsetTextSecondary,
  onSecondary = Colors.SunsetTextSecondary,
  surface = Color.White,
  onSurface = Color.Black,
  background = Colors.SunsetBackground,
  onBackground = Color.Black
)

val sunsetBackgroundGradient = Brush.verticalGradient(
  colors = listOf(Colors.SunsetOrange, Colors.SunsetBlue)
)

private class SunsetRippleTheme(
  val appTheme: AppTheme
) : RippleTheme {
  @Composable
  override fun defaultColor(): Color {
    return MaterialTheme.colorScheme.onSurface
  }

  @Composable
  override fun rippleAlpha(): RippleAlpha {
    return RippleTheme.defaultRippleAlpha(
      Color.Black,
      lightTheme = appTheme.isLight
    )
  }
}
