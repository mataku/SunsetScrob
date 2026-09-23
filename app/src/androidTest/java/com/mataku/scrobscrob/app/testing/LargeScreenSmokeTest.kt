package com.mataku.scrobscrob.app.testing

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.mataku.scrobscrob.app.ui.top.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeScreenE2E
@OptIn(ExperimentalTestApi::class)
class LargeScreenSmokeTest {

  // composeRule must be the OUTER rule so its activity teardown runs AFTER
  // screenshotRule.failed — otherwise the screenshot is captured against an
  // already-destroyed Activity and comes back blank. JUnit applies lower
  // `order` values further out, so composeRule takes the lower value.
  @get:Rule(order = 0)
  val composeRule = createAndroidComposeRule<MainActivity>()

  @get:Rule(order = 1)
  val screenshotRule = TestScreenshotRule()

  @Before
  fun setUp() {
    val instrumentation = InstrumentationRegistry.getInstrumentation()
    val targetContext = instrumentation.targetContext
    resetDataStores(targetContext)

    // Force landscape so the window size class resolves to expanded
    // and the SunsetListDetailScaffold opens its 2-pane layout.
    val device = UiDevice.getInstance(instrumentation)
    device.setOrientationLandscape()
  }

  @Test
  fun login_then_navigate_through_tabs_and_details() {
    // Login screen: the fake launcher returns a token immediately on tap.
    composeRule.waitUntilExactlyOneExists(hasText("Sign in with Last.fm"), TIMEOUT_MS)
    composeRule.onNodeWithText("Sign in with Last.fm").performClick()

    // Home renders with the Scrobble tab as default.
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)

    // Scrobble tab: tap the first recent track to trigger selectDetail
    // on the SunsetListDetailScaffold.
    composeRule.waitUntilExactlyOneExists(hasText("Supernova"), TIMEOUT_MS)
    composeRule.waitUntilExactlyOneExists(hasText("TRACE"), TIMEOUT_MS)

    // Check the second cell id displayed
    composeRule.onNodeWithText("Ummet Ozcan").assertIsDisplayed()

    composeRule.onNodeWithText("TRACE").performClick()

    // Detail pane resolves and renders the track artwork.
    composeRule.waitUntilExactlyOneExists(hasContentDescription("artwork image"), TIMEOUT_MS)

    // Tablet expectation: list (track row) and detail (artwork) are
    // both attached to the composition at the same time. "TRACE" appears
    // twice — once in the list row, once in the detail-pane track header.
    composeRule.waitUntilNodeCount(hasText("TRACE"), 2, TIMEOUT_MS)
    composeRule.onNodeWithContentDescription("artwork image").assertIsDisplayed()
    composeRule.onNodeWithText("Listeners").assertIsDisplayed()
    composeRule.onNodeWithText("Ummet Ozcan").assertIsDisplayed()

    pressBack()

    // Tablet expectation: list (album row) and detail (artwork) are
    // both attached to the composition at the same time.
    // Wait for the pager to settle on the Album page instead of sleeping:
    // on a software-rendered tablet emulator the page scroll can outlast a
    // fixed delay, leaving "ZENITH" composed but not yet clickable.
    composeRule.waitUntilExactlyOneExists(hasText("Album"), TIMEOUT_MS)
    composeRule.onNodeWithText("Album").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("ZENITH"), TIMEOUT_MS)
    composeRule.onNodeWithText("ZENITH").performClick()

    // Album detail is fetched asynchronously; wait for it before asserting.
    composeRule.waitUntilExactlyOneExists(hasText("Track list"), TIMEOUT_MS)
    composeRule.waitUntilNodeCount(hasText("ZENITH"), 2, TIMEOUT_MS) // list + detail
    composeRule.onNodeWithText("欅坂46").assertIsDisplayed()
    composeRule.onNodeWithText("Track list").assertIsDisplayed()
  }

  private fun pressBack() {
    composeRule.runOnUiThread {
      composeRule.activity.onBackPressedDispatcher.onBackPressed()
    }
  }

  private companion object {
    const val TIMEOUT_MS = 5_000L
  }
}
