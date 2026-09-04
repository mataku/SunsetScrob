package com.mataku.scrobscrob.auth.webauth

import androidx.browser.customtabs.CustomTabsIntent
import kotlin.math.roundToInt

internal object WebAuthCustomTabs {
  private const val SHEET_HEIGHT_RATIO = 0.9f
  private const val TOOLBAR_CORNER_RADIUS_DP = 16

  fun sheetHeightPx(containerHeightPx: Int): Int =
    (containerHeightPx * SHEET_HEIGHT_RATIO).roundToInt()

  fun intent(heightPx: Int): CustomTabsIntent =
    CustomTabsIntent.Builder()
      .setInitialActivityHeightPx(heightPx, CustomTabsIntent.ACTIVITY_HEIGHT_FIXED)
      .setToolbarCornerRadiusDp(TOOLBAR_CORNER_RADIUS_DP)
      .build()
}
