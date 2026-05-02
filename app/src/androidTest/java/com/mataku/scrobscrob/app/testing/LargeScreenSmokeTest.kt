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
import androidx.test.uiautomator.UiDevice
import com.mataku.scrobscrob.app.ui.top.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeScreenE2E
@OptIn(ExperimentalTestApi::class)
class LargeScreenSmokeTest {

  @get:Rule
  val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    val instrumentation = InstrumentationRegistry.getInstrumentation()
    val targetContext = instrumentation.targetContext
    resetDataStores(targetContext)

    val device = UiDevice.getInstance(instrumentation)
    device.setOrientationLandscape()
  }

  @Test
  fun scrobble_tap_shows_list_and_detail_panes_simultaneously() {
    composeRule.waitUntilExactlyOneExists(hasText("Let me in!"), TIMEOUT_MS)
    composeRule.onAllNodes(hasSetTextAction()).onFirst().performTextInput("e2e_user")
    composeRule.onAllNodes(hasSetTextAction())[1].performTextInput("e2e_password")
    composeRule.onNodeWithText("Let me in!").performClick()

    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)
    Thread.sleep(STEP_DELAY_MS)

    composeRule.waitUntilExactlyOneExists(hasText("TRACE"), TIMEOUT_MS)
    composeRule.onNodeWithText("TRACE").performClick()

    composeRule.waitUntilExactlyOneExists(hasContentDescription("artwork image"), TIMEOUT_MS)

    composeRule.onNodeWithText("TRACE").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("artwork image").assertIsDisplayed()
  }

  private companion object {
    const val TIMEOUT_MS = 5_000L
    const val STEP_DELAY_MS = 2_000L
  }
}
