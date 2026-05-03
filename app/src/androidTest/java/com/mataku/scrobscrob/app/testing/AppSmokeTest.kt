package com.mataku.scrobscrob.app.testing

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.mataku.scrobscrob.app.ui.top.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class AppSmokeTest {

  // composeRule must be the OUTER rule (higher order) so its activity
  // teardown runs AFTER screenshotRule.failed — otherwise the screenshot
  // is captured against an already-destroyed Activity and comes back blank.
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
  fun login_then_navigate_through_tabs_and_details() {
    // Login screen: fill username/password and submit.
    composeRule.waitUntilExactlyOneExists(hasText("Let me in!"), TIMEOUT_MS)
    composeRule.onAllNodes(hasSetTextAction()).onFirst().performTextInput("e2e_user")
    composeRule.onAllNodes(hasSetTextAction())[1].performTextInput("e2e_password")
    composeRule.onNodeWithText("Let me in!").performClick()

    // Home renders with the Scrobble tab as default.
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)
    composeRule.onNodeWithText("Home").assertIsDisplayed()
    Thread.sleep(STEP_DELAY_MS)

    // Scrobble tab: tap the first recent track and verify TrackScreen opens, then back.
    composeRule.waitUntilExactlyOneExists(hasText("TRACE"), TIMEOUT_MS)
    composeRule.onNodeWithText("TRACE").performClick()
    composeRule.waitUntilExactlyOneExists(hasContentDescription("artwork image"), TIMEOUT_MS)
    Thread.sleep(STEP_DELAY_MS)
    pressBack()
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)
    Thread.sleep(STEP_DELAY_MS)

    // Switch to Album tab on Home, tap an album, verify AlbumScreen opens, then back.
    composeRule.onNodeWithText("Album").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("ZENITH"), TIMEOUT_MS)
    Thread.sleep(STEP_DELAY_MS)
    // Go to album detail
    composeRule.onNodeWithText("ZENITH").performClick()
    composeRule.waitUntilExactlyOneExists(hasContentDescription("artwork image"), TIMEOUT_MS)
    Thread.sleep(STEP_DELAY_MS)

    // Scroll the album detail content to the bottom.
    composeRule.waitUntilExactlyOneExists(hasText("About ZENITH"), TIMEOUT_MS)
    composeRule
      .onNode(hasScrollAction() and hasAnyDescendant(hasText("About ZENITH")))
      .performScrollToNode(hasText("About ZENITH"))
    Thread.sleep(STEP_DELAY_MS)

    pressBack()
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)
    Thread.sleep(STEP_DELAY_MS)

    // Bottom nav: Discover tab renders.
    composeRule.onNodeWithContentDescription("tab Discover").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("Discover"), TIMEOUT_MS)
    composeRule.onNodeWithText("Discover").assertIsDisplayed()
    Thread.sleep(STEP_DELAY_MS)

    // Bottom nav: Account tab renders.
    composeRule.onNodeWithContentDescription("tab Account").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("Account"), TIMEOUT_MS)
    composeRule.onNodeWithText("Account").assertIsDisplayed()
  }

  private fun pressBack() {
    composeRule.runOnUiThread {
      composeRule.activity.onBackPressedDispatcher.onBackPressed()
    }
  }

  private companion object {
    const val TIMEOUT_MS = 2_000L
    const val STEP_DELAY_MS = 1_000L
  }
}
