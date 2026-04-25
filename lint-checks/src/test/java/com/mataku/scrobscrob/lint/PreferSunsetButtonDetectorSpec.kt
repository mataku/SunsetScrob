package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetButtonDetectorSpec : DescribeSpec({

  describe("PreferSunsetButtonDetector") {

    it("material3 Button import inside :ui_common is allowed") {
      lint()
        .files(
          material3ButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.Button

              fun render() {
                Button(onClick = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 Button import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3ButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.molecule

              import androidx.compose.material3.Button

              fun render() {
                Button(onClick = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 Button import inside a feature module is reported") {
      lint()
        .files(
          material3ButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.Button

              fun render() {
                Button(onClick = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 Button import inside :app is reported") {
      lint()
        .files(
          material3ButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.Button

              fun render() {
                Button(onClick = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetButton\") opts out") {
      lint()
        .files(
          material3ButtonStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetButton")

              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.Button

              fun render() {
                Button(onClick = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetButtonDetector.ISSUE)
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

              fun TextButton(onClick: () -> Unit) {}
            """.trimIndent(),
          ),
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.TextButton

              fun render() {
                TextButton(onClick = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
