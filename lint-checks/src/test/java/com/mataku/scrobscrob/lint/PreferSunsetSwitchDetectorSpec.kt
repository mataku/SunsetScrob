package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetSwitchDetectorSpec : DescribeSpec({

  describe("PreferSunsetSwitchDetector") {

    it("material3 Switch import inside :ui_common is allowed") {
      lint()
        .files(
          material3SwitchStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.Switch

              fun render() {
                Switch(checked = false, onCheckedChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSwitchDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 Switch import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3SwitchStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.component

              import androidx.compose.material3.Switch

              fun render() {
                Switch(checked = false, onCheckedChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSwitchDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 Switch import inside a feature module is reported") {
      lint()
        .files(
          material3SwitchStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.account

              import androidx.compose.material3.Switch

              fun render() {
                Switch(checked = false, onCheckedChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSwitchDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 Switch import inside :app is reported") {
      lint()
        .files(
          material3SwitchStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.Switch

              fun render() {
                Switch(checked = false, onCheckedChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSwitchDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetSwitch\") opts out") {
      lint()
        .files(
          material3SwitchStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetSwitch")

              package com.mataku.scrobscrob.feature.account

              import androidx.compose.material3.Switch

              fun render() {
                Switch(checked = false, onCheckedChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSwitchDetector.ISSUE)
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
              package com.mataku.scrobscrob.feature.account

              import androidx.compose.material3.Checkbox

              fun render() {
                Checkbox(checked = false, onCheckedChange = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSwitchDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
