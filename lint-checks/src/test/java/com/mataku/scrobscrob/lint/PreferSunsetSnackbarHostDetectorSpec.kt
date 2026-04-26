package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetSnackbarHostDetectorSpec : DescribeSpec({

  describe("PreferSunsetSnackbarHostDetector") {

    it("material3 SnackbarHost import inside :ui_common is allowed") {
      lint()
        .files(
          material3SnackbarHostStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.SnackbarHost
              import androidx.compose.material3.SnackbarHostState

              fun render(state: SnackbarHostState) {
                SnackbarHost(hostState = state)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSnackbarHostDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 SnackbarHost import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3SnackbarHostStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.style

              import androidx.compose.material3.SnackbarHostState

              fun stateFactory(): SnackbarHostState = SnackbarHostState()
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSnackbarHostDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 SnackbarHost imports inside a feature module are reported") {
      lint()
        .files(
          material3SnackbarHostStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.account

              import androidx.compose.material3.SnackbarHost
              import androidx.compose.material3.SnackbarHostState

              fun render(state: SnackbarHostState) {
                SnackbarHost(hostState = state)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSnackbarHostDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(2)
    }

    it("material3 SnackbarHost import inside :app is reported") {
      lint()
        .files(
          material3SnackbarHostStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.screen

              import androidx.compose.material3.SnackbarHost

              fun render() {}
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSnackbarHostDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetSnackbarHost\") opts out") {
      lint()
        .files(
          material3SnackbarHostStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetSnackbarHost")

              package com.mataku.scrobscrob.feature.account

              import androidx.compose.material3.SnackbarHost
              import androidx.compose.material3.SnackbarHostState

              fun render(state: SnackbarHostState) {
                SnackbarHost(hostState = state)
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSnackbarHostDetector.ISSUE)
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
              package com.mataku.scrobscrob.feature.account

              import androidx.compose.material3.MaterialTheme

              fun render() {
                MaterialTheme(content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetSnackbarHostDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
