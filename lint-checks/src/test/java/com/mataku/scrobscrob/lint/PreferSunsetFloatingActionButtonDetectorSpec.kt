package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetFloatingActionButtonDetectorSpec : DescribeSpec({

  describe("PreferSunsetFloatingActionButtonDetector") {

    it("material3 FloatingActionButton import inside :ui_common is allowed") {
      lint()
        .files(
          material3FloatingActionButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.FloatingActionButton

              fun render() {
                FloatingActionButton(onClick = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetFloatingActionButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 FloatingActionButton import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3FloatingActionButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.component

              import androidx.compose.material3.FloatingActionButton

              fun render() {
                FloatingActionButton(onClick = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetFloatingActionButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 FloatingActionButton import inside a feature module is reported") {
      lint()
        .files(
          material3FloatingActionButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.discover

              import androidx.compose.material3.FloatingActionButton

              fun render() {
                FloatingActionButton(onClick = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetFloatingActionButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 FloatingActionButtonDefaults import inside :app is reported") {
      lint()
        .files(
          material3FloatingActionButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.FloatingActionButtonDefaults

              fun render() = FloatingActionButtonDefaults.shape
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetFloatingActionButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetFloatingActionButton\") opts out") {
      lint()
        .files(
          material3FloatingActionButtonStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetFloatingActionButton")

              package com.mataku.scrobscrob.feature.discover

              import androidx.compose.material3.FloatingActionButton

              fun render() {
                FloatingActionButton(onClick = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetFloatingActionButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("other material3 imports are not reported") {
      lint()
        .files(
          kotlin(
            """
              package androidx.compose.material3

              fun Checkbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {}
            """.trimIndent(),
          ),
          kotlin(
            """
              package com.mataku.scrobscrob.feature.discover

              import androidx.compose.material3.Checkbox

              fun render() {
                Checkbox(checked = false, onCheckedChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetFloatingActionButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
