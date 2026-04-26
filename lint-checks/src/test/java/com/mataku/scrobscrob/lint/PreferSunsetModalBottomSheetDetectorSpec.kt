package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetModalBottomSheetDetectorSpec : DescribeSpec({

  describe("PreferSunsetModalBottomSheetDetector") {

    it("material3 ModalBottomSheet imports inside :ui_common are allowed") {
      lint()
        .files(
          material3ModalBottomSheetStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.ModalBottomSheet
              import androidx.compose.material3.SheetState
              import androidx.compose.material3.rememberModalBottomSheetState

              fun render() {
                rememberModalBottomSheetState()
                val state: SheetState? = null
                ModalBottomSheet(onDismissRequest = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetModalBottomSheetDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 ModalBottomSheet import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3ModalBottomSheetStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.component

              import androidx.compose.material3.ModalBottomSheet

              fun render() {
                ModalBottomSheet(onDismissRequest = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetModalBottomSheetDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 ModalBottomSheet import inside a feature module is reported") {
      lint()
        .files(
          material3ModalBottomSheetStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.ModalBottomSheet

              fun render() {
                ModalBottomSheet(onDismissRequest = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetModalBottomSheetDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 SheetState import inside :app is reported") {
      lint()
        .files(
          material3ModalBottomSheetStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.SheetState

              fun render() {
                val state: SheetState? = null
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetModalBottomSheetDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetModalBottomSheet\") opts out") {
      lint()
        .files(
          material3ModalBottomSheetStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetModalBottomSheet")

              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.ModalBottomSheet

              fun render() {
                ModalBottomSheet(onDismissRequest = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetModalBottomSheetDetector.ISSUE)
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

              fun BottomSheetScaffold(sheetContent: () -> Unit, content: () -> Unit) {}
            """.trimIndent(),
          ),
          kotlin(
            """
              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.BottomSheetScaffold

              fun render() {
                BottomSheetScaffold(sheetContent = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetModalBottomSheetDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
