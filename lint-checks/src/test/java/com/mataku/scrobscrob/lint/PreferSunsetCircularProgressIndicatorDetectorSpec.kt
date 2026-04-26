package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetCircularProgressIndicatorDetectorSpec : DescribeSpec({

  describe("PreferSunsetCircularProgressIndicatorDetector") {

    it("material3 CircularProgressIndicator import inside :ui_common is allowed") {
      lint()
        .files(
          material3CircularProgressIndicatorStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.CircularProgressIndicator

              fun render() {
                CircularProgressIndicator()
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetCircularProgressIndicatorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 CircularProgressIndicator import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3CircularProgressIndicatorStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.component

              import androidx.compose.material3.CircularProgressIndicator

              fun render() {
                CircularProgressIndicator()
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetCircularProgressIndicatorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 CircularProgressIndicator import inside a feature module is reported") {
      lint()
        .files(
          material3CircularProgressIndicatorStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.auth

              import androidx.compose.material3.CircularProgressIndicator

              fun render() {
                CircularProgressIndicator()
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetCircularProgressIndicatorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 CircularProgressIndicator import inside :app is reported") {
      lint()
        .files(
          material3CircularProgressIndicatorStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.CircularProgressIndicator

              fun render() {
                CircularProgressIndicator()
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetCircularProgressIndicatorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetCircularProgressIndicator\") opts out") {
      lint()
        .files(
          material3CircularProgressIndicatorStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetCircularProgressIndicator")

              package com.mataku.scrobscrob.feature.auth

              import androidx.compose.material3.CircularProgressIndicator

              fun render() {
                CircularProgressIndicator()
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetCircularProgressIndicatorDetector.ISSUE)
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

              fun LinearProgressIndicator() {}
            """.trimIndent(),
          ),
          kotlin(
            """
              package com.mataku.scrobscrob.feature.auth

              import androidx.compose.material3.LinearProgressIndicator

              fun render() {
                LinearProgressIndicator()
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetCircularProgressIndicatorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
