package com.mataku.scrobscrob.app.testing

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.mataku.scrobscrob.app.ui.top.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeScreenE2E
@OptIn(ExperimentalTestApi::class)
class LargeScreenSmokeTest {

  // composeRule must be the OUTER rule (higher order) so its activity
  // teardown runs AFTER screenshotRule.failed — otherwise the screenshot
  // is captured against an already-destroyed Activity and comes back blank.
  @get:Rule(order = 1)
  val composeRule = createAndroidComposeRule<MainActivity>()

  @get:Rule(order = 0)
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
    // Login screen: fill username/password and submit.
    composeRule.waitUntilExactlyOneExists(hasText("Let me in!"), TIMEOUT_MS)
    composeRule.onAllNodes(hasSetTextAction()).onFirst().performTextInput("e2e_user")
    composeRule.onAllNodes(hasSetTextAction())[1].performTextInput("e2e_password")
    composeRule.onNodeWithText("Let me in!").performClick()

    // Home renders with the Scrobble tab as default.
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)
    Thread.sleep(STEP_DELAY_MS)

    // Scrobble tab: tap the first recent track to trigger selectDetail
    // on the SunsetListDetailScaffold.
    composeRule.waitUntilExactlyOneExists(hasText("TRACE"), TIMEOUT_MS)

    // Check the second cell id displayed
    composeRule.onNodeWithText("Ummet Ozcan").assertIsDisplayed()

    composeRule.onNodeWithText("TRACE").performClick()

    // Detail pane resolves and renders the track artwork.
    composeRule.waitUntilExactlyOneExists(hasContentDescription("artwork image"), TIMEOUT_MS)

    // Tablet expectation: list (track row) and detail (artwork) are
    // both attached to the composition at the same time. "TRACE" appears
    // twice — once in the list row, once in the detail-pane track header.
    composeRule.onAllNodes(hasText("TRACE")).assertCountEquals(2)
    composeRule.onNodeWithContentDescription("artwork image").assertIsDisplayed()
    composeRule.onNodeWithText("Listeners").assertIsDisplayed()
    composeRule.onNodeWithText("Ummet Ozcan").assertIsDisplayed()

    pressBack()

    // Tablet expectation: list (album row) and detail (artwork) are
    // both attached to the composition at the same time.
    composeRule.onNodeWithText("Album").performClick()
    Thread.sleep(STEP_DELAY_MS)
    composeRule.onNodeWithText("ZENITH").performClick()
    composeRule.onNodeWithText("欅坂46").assertIsDisplayed()
    composeRule.onAllNodes(hasText("ZENITH")).assertCountEquals(2) // list + detail
    composeRule.onNodeWithText("Track list").assertIsDisplayed()
  }

  private fun pressBack() {
    composeRule.runOnUiThread {
      composeRule.activity.onBackPressedDispatcher.onBackPressed()
    }
  }

  private companion object {
    const val TIMEOUT_MS = 5_000L
    const val STEP_DELAY_MS = 2_000L
  }
}
