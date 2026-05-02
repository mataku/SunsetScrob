package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import io.kotest.core.spec.style.DescribeSpec

class PreferSunsetListDetailPaneScaffoldDetectorSpec : DescribeSpec({

  describe("PreferSunsetListDetailPaneScaffoldDetector") {

    it("material3 adaptive imports inside :ui_common are allowed") {
      lint()
        .files(
          material3AdaptiveStub,
          material3AdaptiveNavigationStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common

              import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
              import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
              import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator

              fun render() {
                rememberListDetailPaneScaffoldNavigator<String>()
                ListDetailPaneScaffoldRole.Detail
                ListDetailPaneScaffold(listPane = {}, detailPane = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetListDetailPaneScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 adaptive import inside a :ui_common sub-package is allowed") {
      lint()
        .files(
          material3AdaptiveStub,
          kotlin(
            """
              package com.mataku.scrobscrob.ui_common.component.designsystem

              import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold

              fun render() {
                ListDetailPaneScaffold(listPane = {}, detailPane = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetListDetailPaneScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("material3 adaptive import inside a feature module is reported") {
      lint()
        .files(
          material3AdaptiveStub,
          kotlin(
            """
              package com.mataku.scrobscrob.feature.scrobble

              import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold

              fun render() {
                ListDetailPaneScaffold(listPane = {}, detailPane = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetListDetailPaneScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("material3 adaptive navigation import inside :app is reported") {
      lint()
        .files(
          material3AdaptiveNavigationStub,
          kotlin(
            """
              package com.mataku.scrobscrob.app.ui.navigation

              import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator

              fun render() {
                rememberListDetailPaneScaffoldNavigator<String>()
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetListDetailPaneScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectErrorCount(1)
    }

    it("@file:Suppress(\"PreferSunsetListDetailPaneScaffold\") opts out") {
      lint()
        .files(
          material3AdaptiveStub,
          kotlin(
            """
              @file:Suppress("PreferSunsetListDetailPaneScaffold")

              package com.mataku.scrobscrob.feature.scrobble

              import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold

              fun render() {
                ListDetailPaneScaffold(listPane = {}, detailPane = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetListDetailPaneScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }

    it("unrelated material3 imports are not reported") {
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
              package com.mataku.scrobscrob.feature.scrobble

              import androidx.compose.material3.BottomSheetScaffold

              fun render() {
                BottomSheetScaffold(sheetContent = {}, content = {})
              }
            """.trimIndent(),
          ),
        )
        .issues(PreferSunsetListDetailPaneScaffoldDetector.ISSUE)
        .skipTestModes(TestMode.IMPORT_ALIAS)
        .run()
        .expectClean()
    }
  }
})
