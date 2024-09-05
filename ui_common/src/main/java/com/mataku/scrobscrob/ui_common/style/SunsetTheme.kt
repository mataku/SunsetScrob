package com.mataku.scrobscrob.ui_common.style

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunsetTheme(
  theme: AppTheme = AppTheme.DARK,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = theme.colorScheme(),
  ) {
    CompositionLocalProvider(
      LocalRippleConfiguration provides RippleConfiguration(
        color = MaterialTheme.colorScheme.onSurface,
        rippleAlpha = if (theme.isLight) {
          LightRippleAlpha
        } else {
          DarkRippleAlpha
        }
      ),
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

@OptIn(ExperimentalMaterial3Api::class)
val LocalTopAppBarState = staticCompositionLocalOf<TopAppBarScrollBehavior> {
  throw IllegalStateException("TopAppBarScrollBehavior is not provided")
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

// referenced from old RippleTheme implementation
private val LightRippleAlpha = RippleAlpha(
  pressedAlpha = 0.12f,
  focusedAlpha = 0.12f,
  draggedAlpha = 0.08f,
  hoveredAlpha = 0.04f
)

private val DarkRippleAlpha = RippleAlpha(
  pressedAlpha = 0.10f,
  focusedAlpha = 0.12f,
  draggedAlpha = 0.08f,
  hoveredAlpha = 0.04f
)
