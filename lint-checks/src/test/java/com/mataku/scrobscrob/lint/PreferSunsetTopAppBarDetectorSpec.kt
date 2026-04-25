package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetTopAppBarDetectorSpec : DescribeSpec({

  describe("PreferSunsetTopAppBarDetector") {

    it("material3 TopAppBar import inside :ui_common is allowed") {
      lint()
        .files(
          material3TopAppBarStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.TopAppBar
              import androidx.compose.material3.TopAppBarDefaults
              import androidx.compose.material3.TopAppBarScrollBehavior

              fun render() {
                TopAppBar(title = {})
                TopAppBarDefaults.centerAlignedTopAppBarColors()
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTopAppBarDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 TopAppBar import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3TopAppBarStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.style

              import androidx.compose.material3.TopAppBarScrollBehavior

              interface Holder {
                val behavior: TopAppBarScrollBehavior
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTopAppBarDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 TopAppBar imports inside a feature module are reported") {
      lint()
        .files(
          material3TopAppBarStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.home

              import androidx.compose.material3.TopAppBar
              import androidx.compose.material3.TopAppBarDefaults
              import androidx.compose.material3.TopAppBarScrollBehavior

              fun render() {
                TopAppBar(title = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTopAppBarDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(3)
    }

    it("material3 TopAppBar import inside :app is reported") {
      lint()
        .files(
          material3TopAppBarStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.screen

              import androidx.compose.material3.TopAppBar

              fun render() {
                TopAppBar(title = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTopAppBarDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetTopAppBar\") opts out") {
      lint()
        .files(
          material3TopAppBarStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetTopAppBar")

              package com.mataku.scrobscrob.feature.home

              import androidx.compose.material3.TopAppBar
              import androidx.compose.material3.TopAppBarScrollBehavior

              fun render() {
                TopAppBar(title = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTopAppBarDetector.ISSUE)
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

              fun MaterialTheme(content: () -> Unit) {}
            """.trimIndent(),
          ),
          kotlin(
            """
              package com.mataku.scrobscrob.feature.home

              import androidx.compose.material3.MaterialTheme

              fun render() {
                MaterialTheme(content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTopAppBarDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
