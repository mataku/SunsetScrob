package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferLocalAppThemeColorDetectorSpec : DescribeSpec({

  describe("PreferLocalAppThemeColorDetector") {

    it("MaterialTheme.colorScheme inside :ui_common is allowed") {
      lint()
        .files(
          material3ThemeStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.MaterialTheme

              fun render() {
                val color = MaterialTheme.colorScheme.background
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferLocalAppThemeColorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("MaterialTheme.colorScheme inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3ThemeStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.molecule

              import androidx.compose.material3.MaterialTheme

              fun render() {
                val color = MaterialTheme.colorScheme.background
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferLocalAppThemeColorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("MaterialTheme.colorScheme inside a feature module is reported") {
      lint()
        .files(
          material3ThemeStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.MaterialTheme

              fun render() {
                val color = MaterialTheme.colorScheme.background
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferLocalAppThemeColorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("MaterialTheme.colorScheme inside :app is reported") {
      lint()
        .files(
          material3ThemeStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.MaterialTheme

              fun render() {
                val color = MaterialTheme.colorScheme.onSurface
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferLocalAppThemeColorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferLocalAppThemeColor\") opts out") {
      lint()
        .files(
          material3ThemeStub,
          kotlin(
            """
              @file:Suppress("PreferLocalAppThemeColor")

              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.MaterialTheme

              fun render() {
                val color = MaterialTheme.colorScheme.background
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferLocalAppThemeColorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("other MaterialTheme references are not reported") {
      lint()
        .files(
          material3ThemeStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.MaterialTheme

              fun render() {
                val theme = MaterialTheme
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferLocalAppThemeColorDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
