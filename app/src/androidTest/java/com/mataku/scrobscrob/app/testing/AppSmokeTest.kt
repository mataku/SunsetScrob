package com.mataku.scrobscrob.app.testing

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.platform.app.InstrumentationRegistry
import com.mataku.scrobscrob.app.ui.top.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class AppSmokeTest {

  // composeRule must be the OUTER rule so its activity teardown runs AFTER
  // screenshotRule.failed — otherwise the screenshot is captured against an
  // already-destroyed Activity and comes back blank. JUnit applies lower
  // `order` values further out, so composeRule takes the lower value.
  @get:Rule(order = 0)
  val composeRule = createAndroidComposeRule<MainActivity>()

  @get:Rule(order = 1)
  val screenshotRule = TestScreenshotRule()

  @Before
  fun resetState() {
    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    resetDataStores(targetContext)
  }

  @Test
  fun login_then_navigate_through_tabs_and_details_then_logout() {
    // Login screen: the fake launcher returns a token immediately on tap.
    composeRule.waitUntilExactlyOneExists(hasText(signInLabel()), TIMEOUT_MS)
    composeRule.onNodeWithText(signInLabel()).performClick()

    // Home renders with the Scrobble tab as default.
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)
    composeRule.onNodeWithText("Home").assertIsDisplayed()

    // Scrobble tab: tap the first recent track and verify TrackScreen opens, then back.
    composeRule.waitUntilExactlyOneExists(hasText("Supernova"), TIMEOUT_MS)
    composeRule.waitUntilExactlyOneExists(hasText("TRACE"), TIMEOUT_MS)
    composeRule.onNodeWithText("TRACE").performClick()
    composeRule.waitUntilExactlyOneExists(hasContentDescription("artwork image"), TIMEOUT_MS)
    pressBack()
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)

    // Switch to Album tab on Home, tap an album, verify AlbumScreen opens, then back.
    composeRule.onNodeWithText("Album").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("ZENITH"), TIMEOUT_MS)
    // Go to album detail
    composeRule.onNodeWithText("ZENITH").performClick()
    composeRule.waitUntilExactlyOneExists(hasContentDescription("artwork image"), TIMEOUT_MS)

    // Scroll the album detail content to the bottom.
    composeRule.waitUntilExactlyOneExists(hasText("About ZENITH"), TIMEOUT_MS)
    composeRule
      .onNode(hasScrollAction() and hasAnyDescendant(hasText("About ZENITH")))
      .performScrollToNode(hasText("About ZENITH"))

    pressBack()
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)

    // Switch to Artist tab on Home, tap an artist, verify ArtistScreen opens.
    // "About PassCode" is the wiki cell, which only renders once artist.getInfo is parsed.
    composeRule.onNodeWithText("Artist").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("PassCode"), TIMEOUT_MS)
    composeRule.onNodeWithText("PassCode").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("About PassCode"), TIMEOUT_MS)

    // Recreate the Activity on the detail screen.
    // The Navigation 3 back stack must restore the same destination from its @Serializable key.
    composeRule.activityRule.scenario.recreate()
    composeRule.waitUntilExactlyOneExists(hasText("About PassCode"), TIMEOUT_MS)

    pressBack()
    composeRule.waitUntilExactlyOneExists(hasText("Home"), TIMEOUT_MS)

    // Bottom nav: Discover renders the chart.getTopTracks / chart.getTopArtists fixtures.
    composeRule.onNodeWithContentDescription("tab Discover").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("My Love Mine All Mine"), TIMEOUT_MS)
    composeRule.waitUntilExactlyOneExists(hasText("The Weeknd"), TIMEOUT_MS)

    // Bottom nav: Account renders the user.getInfo fixture in the profile header.
    composeRule.onNodeWithContentDescription("tab Account").performClick()
    composeRule.waitUntilExactlyOneExists(hasText("smoke_user"), TIMEOUT_MS)
    composeRule.onNodeWithText("smoke_user").assertIsDisplayed()

    // Account → Privacy policy: the only step that builds a real android.webkit.WebView, which the
    // JVM screenshot tests replace with an empty Box. The page itself is remote, so nothing waits
    // for it to load; reaching the screen without a crash is the assertion.
    val privacyPolicy = privacyPolicyLabel()
    composeRule
      .onNode(hasScrollAction() and hasAnyDescendant(hasText("smoke_user")))
      .performScrollToNode(hasText(privacyPolicy))
    composeRule.onNodeWithText(privacyPolicy).performClick()
    // The menu row and the screen title share their text, so the back icon is what tells the
    // destination apart from the Account list.
    composeRule.waitUntilExactlyOneExists(hasContentDescription("Back"), TIMEOUT_MS)
    composeRule.waitUntilExactlyOneExists(hasText(privacyPolicy), TIMEOUT_MS)

    pressBack()
    composeRule.waitUntilDoesNotExist(hasContentDescription("Back"), TIMEOUT_MS)

    // Account → Logout: resetDataStores() logs out behind the UI before every test, so this is the
    // only place the dialog, AccountViewModel.logout and the authentication gate run end to end.
    // The list may come back scrolled to either end, so match it by whichever anchor is composed.
    val logout = logoutLabel()
    composeRule
      .onNode(
        hasScrollAction() and
          (hasAnyDescendant(hasText("smoke_user")) or hasAnyDescendant(hasText(privacyPolicy))),
      )
      .performScrollToNode(hasText(logout))
    composeRule.onNodeWithText(logout).performClick()
    composeRule.waitUntilExactlyOneExists(hasText("Let me out!"), TIMEOUT_MS)
    composeRule.onNodeWithText("Let me out!").performClick()
    composeRule.waitUntilExactlyOneExists(hasText(signInLabel()), TIMEOUT_MS)
  }

  private fun pressBack() {
    composeRule.runOnUiThread {
      composeRule.activity.onBackPressedDispatcher.onBackPressed()
    }
  }

  private companion object {
    const val TIMEOUT_MS = 2_000L
  }
}
