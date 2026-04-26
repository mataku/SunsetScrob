package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetScaffoldDetectorSpec : DescribeSpec({

  describe("PreferSunsetScaffoldDetector") {

    it("material3 Scaffold import inside :ui_common is allowed") {
      lint()
        .files(
          material3ScaffoldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.Scaffold

              fun render() {
                Scaffold(content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 Scaffold import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3ScaffoldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.molecule

              import androidx.compose.material3.Scaffold

              fun render() {
                Scaffold(content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 Scaffold import inside a feature module is reported") {
      lint()
        .files(
          material3ScaffoldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.Scaffold

              fun render() {
                Scaffold(content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 Scaffold import inside :app is reported") {
      lint()
        .files(
          material3ScaffoldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.screen

              import androidx.compose.material3.Scaffold

              fun render() {
                Scaffold(content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetScaffold\") opts out") {
      lint()
        .files(
          material3ScaffoldStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetScaffold")

              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.Scaffold

              fun render() {
                Scaffold(content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetScaffoldDetector.ISSUE)
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

              fun Surface(content: () -> Unit) {}
            """.trimIndent(),
          ),
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.Surface

              fun render() {
                Surface(content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
