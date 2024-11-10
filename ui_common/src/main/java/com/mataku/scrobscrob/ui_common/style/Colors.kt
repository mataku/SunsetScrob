package com.mataku.scrobscrob.ui_common.style

import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseColor

object Colors {
  val ContentBackground = Color(0xFF263238)
  val LastFmColor = Color(0xFFbb0414)
  val BarBackground = Color(0xFF607D8B)
  val textSecondary = Color(0xFF90A4AE)
  val textSecondaryLight = Color(0xFF757575)
  val Lime = Color(0xFFCDDC39)
  val LightLime = Color(0xFFC0CA33)
  val MidnightGray = Color(0xFF424242)
  val DeepOcean = Color(0xFF0277FF)
  val DeepOceanBackground = Color(0xFF191c38)
  val SunsetOrange = Color(0xFFFFB74D)
  val SunsetBlue = Color(0xFF90CAF9)
  val SunsetBackground = Color(0xFFf5b95c)
  val Heart = Color(0xFFEC407A)
  val StatusBarDark = Color(0x1A000000)
  val StatusBarLight = Color(0x1AFFFFFF)
}

object DarkColor {
  @ShowkaseColor(name = "primary", group = "Dark")
  val primary = Color.DarkGray

  @ShowkaseColor(name = "onPrimary", group = "Dark")
  val onPrimary = Color.White

  @ShowkaseColor(name = "secondary", group = "Dark")
  val secondary = Color.White

  @ShowkaseColor(name = "onSecondary", group = "Dark")
  val onSecondary = Colors.textSecondary

  @ShowkaseColor(name = "surface", group = "Dark")
  val surface = Colors.ContentBackground

  @ShowkaseColor(name = "onSurface", group = "Dark")
  val onSurface = Color.White

  @ShowkaseColor(name = "background", group = "Dark")
  val background = Colors.ContentBackground

  @ShowkaseColor(name = "onBackground", group = "Dark")
  val onBackground = Color.White
}

object LightColor {
  @ShowkaseColor(name = "primary", group = "Light")
  val primary = Color(0xFFEEEEEE)

  @ShowkaseColor(name = "onPrimary", group = "Light")
  val onPrimary = Color.Black

  @ShowkaseColor(name = "secondary", group = "Light")
  val secondary = Color.White

  @ShowkaseColor(name = "onSecondary", group = "Light")
  val onSecondary = Colors.textSecondaryLight

  @ShowkaseColor(name = "surface", group = "Light")
  val surface = Color(0xFFF5F5F5)

  @ShowkaseColor(name = "onSurface", group = "Light")
  val onSurface = Color.Black

  @ShowkaseColor(name = "background", group = "Light")
  val background = Color(0xFFF5F5F5)

  @ShowkaseColor(name = "onBackground", group = "Light")
  val onBackground = Color.Black
}

object LastFmDarkColor {
  @ShowkaseColor(name = "primary", group = "Last.fm Dark")
  val primary = Color.DarkGray

  @ShowkaseColor(name = "onPrimary", group = "Last.fm Dark")
  val onPrimary = Color.White

  @ShowkaseColor(name = "secondary", group = "Last.fm Dark")
  val secondary = Color.White

  @ShowkaseColor(name = "onSecondary", group = "Last.fm Dark")
  val onSecondary = Colors.textSecondary

  @ShowkaseColor(name = "surface", group = "Last.fm Dark")
  val surface = Color(0xFF37474F)

  @ShowkaseColor(name = "onSurface", group = "Last.fm Dark")
  val onSurface = Color.White

  @ShowkaseColor(name = "background", group = "Last.fm Dark")
  val background = Colors.ContentBackground

  @ShowkaseColor(name = "onBackground", group = "Last.fm Dark")
  val onBackground = Color.White
}

