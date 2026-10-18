package com.mataku.scrobscrob.account.ui.screen

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class PrivacyPolicyScreenTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        PrivacyPolicyScreen(onBackPressed = {})
      },
      fileName = "privacy_policy_screen.png"
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        PrivacyPolicyScreen(onBackPressed = {})
      },
      fileName = "privacy_policy_screen_light.png"
    )
  }
}
