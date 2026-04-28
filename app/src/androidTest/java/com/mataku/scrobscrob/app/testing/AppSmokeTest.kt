package com.mataku.scrobscrob.app.testing

import androidx.compose.ui.test.ExperimentalTestApi
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
import com.mataku.scrobscrob.app.ui.top.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class AppSmokeTest {

  @get:Rule
  val composeRule = createAndroidComposeRule<MainActivity>()

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

    // Scrobble tab: tap the first recent track and verify TrackScreen opens, then back.
    composeRule.waitUntilExactlyOneExists(hasText("TRACE"), TIMEOUT_MS)
    composeRule.onNodeWithText("TRACE").performClick()
    composeRule.waitUntilExactlyOneExists(hasContentDescription("artwork image"), TIMEOUT_MS)
    pressBack()
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)

    // Switch to Album tab on Home, tap an album, verify AlbumScreen opens, then back.
    composeRule.onNodeWithText("Album").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("ZENITH"), TIMEOUT_MS)
    composeRule.onNodeWithText("ZENITH").performClick()
    composeRule.waitUntilExactlyOneExists(hasContentDescription("artwork image"), TIMEOUT_MS)
    pressBack()
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)

    // Bottom nav: Discover tab renders.
    composeRule.onNodeWithContentDescription("tab Discover").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("Discover"), TIMEOUT_MS)
    composeRule.onNodeWithText("Discover").assertIsDisplayed()

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
    const val TIMEOUT_MS = 5_000L
  }
}
