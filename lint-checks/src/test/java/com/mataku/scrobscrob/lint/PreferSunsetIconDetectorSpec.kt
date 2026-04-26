package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetIconDetectorSpec : DescribeSpec({

  describe("PreferSunsetIconDetector") {

    it("material3 Icon import inside :ui_common is allowed") {
      lint()
        .files(
          material3IconStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.Icon

              fun render() {
                Icon(imageVector = Any(), contentDescription = null)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 Icon import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3IconStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.component

              import androidx.compose.material3.Icon

              fun render() {
                Icon(imageVector = Any(), contentDescription = null)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 Icon import inside a feature module is reported") {
      lint()
        .files(
          material3IconStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.Icon

              fun render() {
                Icon(imageVector = Any(), contentDescription = null)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 Icon import inside :app is reported") {
      lint()
        .files(
          material3IconStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.Icon

              fun render() {
                Icon(imageVector = Any(), contentDescription = null)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetIcon\") opts out") {
      lint()
        .files(
          material3IconStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetIcon")

              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.Icon

              fun render() {
                Icon(imageVector = Any(), contentDescription = null)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconDetector.ISSUE)
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

              fun Text(text: String) {}
            """.trimIndent(),
          ),
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.Text

              fun render() {
                Text(text = "hello")
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
