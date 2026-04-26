package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetIconToggleButtonDetectorSpec : DescribeSpec({

  describe("PreferSunsetIconToggleButtonDetector") {

    it("material3 IconToggleButton import inside :ui_common is allowed") {
      lint()
        .files(
          material3IconToggleButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.IconToggleButton

              fun render() {
                IconToggleButton(checked = false, onCheckedChange = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconToggleButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 IconToggleButton import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3IconToggleButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.component

              import androidx.compose.material3.IconToggleButton

              fun render() {
                IconToggleButton(checked = false, onCheckedChange = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconToggleButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 IconToggleButton import inside a feature module is reported") {
      lint()
        .files(
          material3IconToggleButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.IconToggleButton

              fun render() {
                IconToggleButton(checked = false, onCheckedChange = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconToggleButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 IconToggleButton import inside :app is reported") {
      lint()
        .files(
          material3IconToggleButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.IconToggleButton

              fun render() {
                IconToggleButton(checked = false, onCheckedChange = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconToggleButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetIconToggleButton\") opts out") {
      lint()
        .files(
          material3IconToggleButtonStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetIconToggleButton")

              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.IconToggleButton

              fun render() {
                IconToggleButton(checked = false, onCheckedChange = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconToggleButtonDetector.ISSUE)
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

              fun IconButton(onClick: () -> Unit, content: () -> Unit) {}
            """.trimIndent(),
          ),
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.IconButton

              fun render() {
                IconButton(onClick = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconToggleButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
