package com.mataku.scrobscrob.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UImportStatement

class PreferSunsetModalBottomSheetDetector : Detector(), SourceCodeScanner {

  override fun getApplicableUastTypes(): List<Class<out UElement>> =
    listOf(UImportStatement::class.java)

  override fun createUastHandler(context: JavaContext): UElementHandler =
    object : UElementHandler() {
      override fun visitImportStatement(node: UImportStatement) {
        check(context, node)
      }
    }

  private fun check(context: JavaContext, node: UImportStatement) {
    val fqn = node.importReference?.asSourceString() ?: return
    if (fqn !in MATERIAL3_MODAL_BOTTOM_SHEET_FQCNS) return

    val pkg = context.uastFile?.packageName.orEmpty()
    if (pkg == UI_COMMON_PACKAGE || pkg.startsWith("$UI_COMMON_PACKAGE.")) return

    context.report(
      issue = ISSUE,
      scope = node,
      location = context.getLocation(node),
      message = "Use `com.mataku.scrobscrob.ui_common.SunsetModalBottomSheet` instead of `$fqn`.",
    )
  }

  companion object {
    private val MATERIAL3_MODAL_BOTTOM_SHEET_FQCNS = setOf(
      "androidx.compose.material3.ModalBottomSheet",
      "androidx.compose.material3.rememberModalBottomSheetState",
      "androidx.compose.material3.SheetState",
    )
    private const val UI_COMMON_PACKAGE = "com.mataku.scrobscrob.ui_common"

    val ISSUE: Issue = Issue.create(
      id = "PreferSunsetModalBottomSheet",
      briefDescription = "Prefer SunsetModalBottomSheet over material3 ModalBottomSheet",
      explanation = """
        Outside of `:ui_common`, prefer the `com.mataku.scrobscrob.ui_common.SunsetModalBottomSheet` \
        wrapper over `androidx.compose.material3.ModalBottomSheet`, \
        `rememberModalBottomSheetState`, and `SheetState`. The wrapper exposes a \
        `SunsetModalBottomSheetState` shim with `isVisible`, `hide()`, and `show()` so \
        feature modules never need to import material3's `SheetState` directly. \
        `contentWindowInsets` is also fixed to `WindowInsets.displayCutout` inside the wrapper. \
        Inside `:ui_common` itself the wrapper has to import these material3 APIs, so files in \
        that package are exempt. Suppress with `@Suppress("PreferSunsetModalBottomSheet")` only \
        when there is a documented reason.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        PreferSunsetModalBottomSheetDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
