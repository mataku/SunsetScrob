package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetTextFieldDetectorSpec : DescribeSpec({

  describe("PreferSunsetTextFieldDetector") {

    it("material3 OutlinedTextField import inside :ui_common is allowed") {
      lint()
        .files(
          material3OutlinedTextFieldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.OutlinedTextField

              fun render() {
                OutlinedTextField(value = "", onValueChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTextFieldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 OutlinedTextField import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3OutlinedTextFieldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.component

              import androidx.compose.material3.OutlinedTextField

              fun render() {
                OutlinedTextField(value = "", onValueChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTextFieldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 OutlinedTextField import inside a feature module is reported") {
      lint()
        .files(
          material3OutlinedTextFieldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.auth

              import androidx.compose.material3.OutlinedTextField

              fun render() {
                OutlinedTextField(value = "", onValueChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTextFieldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 OutlinedTextFieldDefaults import inside :app is reported") {
      lint()
        .files(
          material3OutlinedTextFieldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.OutlinedTextFieldDefaults

              fun render() {
                OutlinedTextFieldDefaults.colors()
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTextFieldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetTextField\") opts out") {
      lint()
        .files(
          material3OutlinedTextFieldStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetTextField")

              package com.mataku.scrobscrob.feature.auth

              import androidx.compose.material3.OutlinedTextField

              fun render() {
                OutlinedTextField(value = "", onValueChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTextFieldDetector.ISSUE)
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
              package com.mataku.scrobscrob.feature.auth

              import androidx.compose.material3.Checkbox

              fun render() {
                Checkbox(checked = false, onCheckedChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTextFieldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
