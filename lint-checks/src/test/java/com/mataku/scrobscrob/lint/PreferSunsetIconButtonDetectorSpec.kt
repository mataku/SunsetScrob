package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetIconButtonDetectorSpec : DescribeSpec({

  describe("PreferSunsetIconButtonDetector") {

    it("material3 IconButton import inside :ui_common is allowed") {
      lint()
        .files(
          material3IconButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.IconButton

              fun render() {
                IconButton(onClick = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 IconButton import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3IconButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.molecule

              import androidx.compose.material3.IconButton

              fun render() {
                IconButton(onClick = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 IconButton import inside a feature module is reported") {
      lint()
        .files(
          material3IconButtonStub,
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
        .issues(PreferSunsetIconButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 IconButton import inside :app is reported") {
      lint()
        .files(
          material3IconButtonStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.IconButton

              fun render() {
                IconButton(onClick = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetIconButton\") opts out") {
      lint()
        .files(
          material3IconButtonStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetIconButton")

              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.IconButton

              fun render() {
                IconButton(onClick = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetIconButtonDetector.ISSUE)
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

              fun Icon(imageVector: Any, contentDescription: String?) {}
            """.trimIndent(),
          ),
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
        .issues(PreferSunsetIconButtonDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
