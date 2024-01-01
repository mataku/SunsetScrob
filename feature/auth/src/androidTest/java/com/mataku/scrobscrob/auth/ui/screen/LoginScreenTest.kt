package com.mataku.scrobscrob.auth.ui.screen

import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.test_helper.integration.assertDisplayableAndClickable
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun layout() = runTest {
    composeRule.setContent {
      SunsetTheme {
        Surface {
          LoginScreen(
            viewModel = LoginViewModel(mockk()),
            navController = mockk()
          )
        }
      }
    }
    composeRule.onNodeWithText("Login to Last.fm").assertIsDisplayed()

    composeRule.onNodeWithText("Let me in!").assertDisplayableAndClickable()
    composeRule.onNodeWithText("Privacy policy").assertDisplayableAndClickable()
    composeRule.onNodeWithText("Username")
      .performTextInput("username")

    composeRule.onNodeWithText("Password")
      .performTextInput("password")
  }
}
