package com.mataku.scrobscrob.app.testing

import android.content.ComponentName
import android.content.Intent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.core.net.toUri
import androidx.test.platform.app.InstrumentationRegistry
import com.mataku.scrobscrob.app.testing.di.E2E_TOKEN
import com.mataku.scrobscrob.app.ui.top.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class WebAuthCallbackTest {

  @get:Rule(order = 1)
  val composeRule = createAndroidComposeRule<MainActivity>()

  @get:Rule(order = 0)
  val screenshotRule = TestScreenshotRule()

  @Before
  fun resetState() {
    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    resetDataStores(targetContext)
  }

  @Test
  fun app_link_callback_logs_in_without_the_browser() {
    composeRule.waitUntilExactlyOneExists(hasText("Sign in with Last.fm"), TIMEOUT_MS)

    val callback = Intent(
      Intent.ACTION_VIEW,
      "https://sunsetscrob.mataku.com/auth/lastfm?token=$E2E_TOKEN".toUri(),
    ).setClass(composeRule.activity, MainActivity::class.java)
    composeRule.activityRule.scenario.onActivity { activity ->
      InstrumentationRegistry.getInstrumentation().callActivityOnNewIntent(activity, callback)
    }

    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)
    composeRule.onNodeWithText("Home").assertIsDisplayed()
  }

  @After
  fun restoreLaunchIntent() {
    composeRule.activityRule.scenario.onActivity { activity ->
      activity.intent = Intent(Intent.ACTION_MAIN)
        .addCategory(Intent.CATEGORY_LAUNCHER)
        .setComponent(ComponentName(activity, MainActivity::class.java))
    }
  }

  private companion object {
    const val TIMEOUT_MS = 5_000L
  }
}
