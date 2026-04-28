package com.mataku.scrobscrob.app.testing

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.mataku.scrobscrob.app.ui.top.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class SmokeFlowTest {

  @get:Rule
  val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun resetState() {
    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    resetDataStores(targetContext)
  }

  @Test
  fun login_redirects_to_home() {
    composeRule.waitUntilExactlyOneExists(hasText("Let me in!"), TIMEOUT_MS)

    composeRule.onAllNodes(hasSetTextAction()).onFirst().performTextInput("e2e_user")
    composeRule.onAllNodes(hasSetTextAction())[1].performTextInput("e2e_password")

    composeRule.onNodeWithText("Let me in!").performClick()

    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)
    composeRule.onNodeWithText("Home").assertIsDisplayed()
  }

  private companion object {
    const val TIMEOUT_MS = 10_000L
  }
}
