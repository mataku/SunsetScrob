package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetTabRowDetectorSpec : DescribeSpec({

  describe("PreferSunsetTabRowDetector") {

    it("material3 TabRow / Tab imports inside :ui_common are allowed") {
      lint()
        .files(
          material3TabRowStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.Tab
              import androidx.compose.material3.TabRow

              fun render() {
                TabRow(selectedTabIndex = 0)
                Tab(selected = false, onClick = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTabRowDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 TabRow import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3TabRowStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.component

              import androidx.compose.material3.TabRow

              fun render() {
                TabRow(selectedTabIndex = 0)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTabRowDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 TabRow import inside a feature module is reported") {
      lint()
        .files(
          material3TabRowStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.home

              import androidx.compose.material3.TabRow

              fun render() {
                TabRow(selectedTabIndex = 0)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTabRowDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 Tab import inside :app is reported") {
      lint()
        .files(
          material3TabRowStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.Tab

              fun render() {
                Tab(selected = false, onClick = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTabRowDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetTabRow\") opts out") {
      lint()
        .files(
          material3TabRowStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetTabRow")

              package com.mataku.scrobscrob.feature.home

              import androidx.compose.material3.TabRow

              fun render() {
                TabRow(selectedTabIndex = 0)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTabRowDetector.ISSUE)
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
              package com.mataku.scrobscrob.feature.home

              import androidx.compose.material3.Checkbox

              fun render() {
                Checkbox(checked = false, onCheckedChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetTabRowDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
