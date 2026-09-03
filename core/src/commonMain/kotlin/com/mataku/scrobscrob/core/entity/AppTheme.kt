package com.mataku.scrobscrob.core.entity

enum class AppTheme(
  val displayName: String,
  val isLight: Boolean,
  val priority: Int
) {
  FOLLOW_SYSTEM("System default", false, 0),
  DARK("Dark", false, 1),
  LIGHT("Light", true, 5),
  MIDNIGHT("Midnight", false, 2),
  OCEAN("Ocean", false, 3),
  LASTFM_DARK("Last.fm Dark", false, 4);

  fun resolve(isSystemDark: Boolean): AppTheme = when (this) {
    FOLLOW_SYSTEM -> if (isSystemDark) DARK else LIGHT
    else -> this
  }
}
