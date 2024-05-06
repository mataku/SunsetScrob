package com.mataku.scrobscrob.ui_common.style

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme

@Composable
fun SunsetTheme(
  theme: AppTheme = AppTheme.DARK,
  content: @Composable () -> Unit,
) {
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

val LocalSnackbarHostState = staticCompositionLocalOf {
  SnackbarHostState()
}

const val ANIMATION_DURATION_MILLIS = 700
const val TRANSITION_ANIMATION_DURATION_MILLIS = 600
val BOTTOM_APP_BAR_HEIGHT = 80.dp

fun AppTheme.backgroundColor(): Color {
  return this.colorScheme().background
}

fun AppTheme.accentColor(): Color {
  return when (this) {
    AppTheme.DARK, AppTheme.LIGHT, AppTheme.MIDNIGHT -> {
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
  }
}

private val lightColorScheme = lightColorScheme(
  primary = LightColor.primary,
  onPrimary = LightColor.onPrimary,
  secondary = LightColor.secondary,
  onSecondary = LightColor.onSecondary,
  surface = LightColor.surface,
  onSurface = LightColor.onSurface,
  background = LightColor.background,
  onBackground = LightColor.onBackground,
)

private val darkColorScheme = darkColorScheme(
  primary = DarkColor.primary,
  onPrimary = DarkColor.onPrimary,
  secondary = DarkColor.secondary,
  onSecondary = DarkColor.onSecondary,
  surface = DarkColor.surface,
  onSurface = DarkColor.onSurface,
  background = DarkColor.background,
  onBackground = DarkColor.onBackground
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
  primary = LastFmDarkColor.primary,
  onPrimary = LastFmDarkColor.onPrimary,
  secondary = LastFmDarkColor.secondary,
  onSecondary = LastFmDarkColor.onSecondary,
  surface = LastFmDarkColor.surface,
  onSurface = LastFmDarkColor.onSurface,
  background = LastFmDarkColor.background,
  onBackground = LastFmDarkColor.onBackground
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
