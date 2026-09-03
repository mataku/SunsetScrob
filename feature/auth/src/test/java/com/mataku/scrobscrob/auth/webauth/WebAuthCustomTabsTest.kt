package com.mataku.scrobscrob.auth.webauth

import androidx.browser.customtabs.CustomTabsIntent
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WebAuthCustomTabsTest {

  @Test
  fun sheetHeightPx_is90PercentOfTheContainer() {
    WebAuthCustomTabs.sheetHeightPx(containerHeightPx = 2000) shouldBe 1800
  }

  @Test
  fun intent_opensAsAFixedHeightBottomSheet() {
    val intent = WebAuthCustomTabs.intent(heightPx = 1800).intent

    intent.getIntExtra(CustomTabsIntent.EXTRA_INITIAL_ACTIVITY_HEIGHT_PX, 0) shouldBe 1800
    intent.getIntExtra(CustomTabsIntent.EXTRA_ACTIVITY_HEIGHT_RESIZE_BEHAVIOR, -1) shouldBe
      CustomTabsIntent.ACTIVITY_HEIGHT_FIXED
  }

  @Test
  fun intent_roundsTheToolbarCorners() {
    val intent = WebAuthCustomTabs.intent(heightPx = 1800).intent

    intent.getIntExtra(CustomTabsIntent.EXTRA_TOOLBAR_CORNER_RADIUS_DP, 0) shouldBe 16
  }
}
