package com.mataku.scrobscrob.auth.webauth

import androidx.browser.customtabs.CustomTabsIntent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import android.app.Activity
import android.app.Application

@RunWith(AndroidJUnit4::class)
class CustomTabsWebAuthLauncherTest {
  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  private val url = "https://www.last.fm/api/auth/?api_key=key&cb=cb"

  @Test
  fun launch_startsTheCustomTabForResultSoChromeCanIdentifyTheCaller() {
    val results = mutableListOf<LastFmWebAuthResult>()
    lateinit var launch: (String) -> Unit
    composeTestRule.setContent {
      launch = CustomTabsWebAuthLauncher().rememberLaunch(onResult = results::add)
    }

    composeTestRule.runOnUiThread { launch(url) }

    val startedForResult = shadowOf(ApplicationProvider.getApplicationContext<Application>())
      .nextStartedActivityForResult
      .shouldNotBeNull()
    startedForResult.requestCode shouldBeGreaterThan -1
    val started = startedForResult.intent
    started.dataString shouldBe url
    started.hasExtra(CustomTabsIntent.EXTRA_SESSION) shouldBe true
    started.getIntExtra(CustomTabsIntent.EXTRA_INITIAL_ACTIVITY_HEIGHT_PX, 0) shouldBeGreaterThan 0
    results.shouldBeEmpty()
  }

  @Test
  fun launch_reportsClosedWhenTheCustomTabFinishes() {
    val results = mutableListOf<LastFmWebAuthResult>()
    lateinit var launch: (String) -> Unit
    composeTestRule.setContent {
      launch = CustomTabsWebAuthLauncher().rememberLaunch(onResult = results::add)
    }

    composeTestRule.runOnUiThread { launch(url) }
    val started = shadowOf(composeTestRule.activity).nextStartedActivityForResult.intent
    composeTestRule.runOnUiThread {
      shadowOf(composeTestRule.activity).receiveResult(started, Activity.RESULT_CANCELED, null)
    }

    results shouldBe listOf(LastFmWebAuthResult.Closed)
  }

  @Test
  fun launch_reportsFailedWhenNoBrowserCanHandleTheIntent() {
    val results = mutableListOf<LastFmWebAuthResult>()
    lateinit var launch: (String) -> Unit
    composeTestRule.setContent {
      launch = CustomTabsWebAuthLauncher().rememberLaunch(onResult = results::add)
    }
    shadowOf(ApplicationProvider.getApplicationContext<Application>()).checkActivities(true)

    composeTestRule.runOnUiThread { launch(url) }

    results shouldBe listOf(LastFmWebAuthResult.Failed)
  }
}
