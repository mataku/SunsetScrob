package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetBottomSheetDetectorSpec : DescribeSpec({

  describe("PreferSunsetBottomSheetDetector") {

    it("material3 BottomSheetScaffold imports inside :ui_common are allowed") {
      lint()
        .files(
          material3BottomSheetScaffoldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.BottomSheetScaffold
              import androidx.compose.material3.SheetValue
              import androidx.compose.material3.rememberBottomSheetScaffoldState
              import androidx.compose.material3.rememberStandardBottomSheetState

              fun render() {
                rememberBottomSheetScaffoldState()
                rememberStandardBottomSheetState()
                SheetValue.PartiallyExpanded
                BottomSheetScaffold(sheetContent = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetBottomSheetDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 BottomSheetScaffold import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3BottomSheetScaffoldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.molecule

              import androidx.compose.material3.BottomSheetScaffold

              fun render() {
                BottomSheetScaffold(sheetContent = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetBottomSheetDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 BottomSheetScaffold import inside a feature module is reported") {
      lint()
        .files(
          material3BottomSheetScaffoldStub,
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
        .issues(PreferSunsetBottomSheetDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 SheetValue import inside :app is reported") {
      lint()
        .files(
          material3BottomSheetScaffoldStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.SheetValue

              fun render() {
                SheetValue.PartiallyExpanded
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetBottomSheetDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetBottomSheet\") opts out") {
      lint()
        .files(
          material3BottomSheetScaffoldStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetBottomSheet")

              package com.mataku.scrobscrob.feature.album

              import androidx.compose.material3.BottomSheetScaffold

              fun render() {
                BottomSheetScaffold(sheetContent = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetBottomSheetDetector.ISSUE)
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

              fun ModalBottomSheet(onDismissRequest: () -> Unit, content: () -> Unit) {}
            """.trimIndent(),
          ),
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
        .issues(PreferSunsetBottomSheetDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
