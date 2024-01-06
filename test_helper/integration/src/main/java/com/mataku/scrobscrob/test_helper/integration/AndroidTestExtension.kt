package com.mataku.scrobscrob.test_helper.integration

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed

fun SemanticsNodeInteraction.assertDisplayableAndClickable() {
  this.assertIsDisplayed()
  this.assertHasClickAction()
}
