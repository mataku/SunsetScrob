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

class PreferSunsetBottomSheetDetector : Detector(), SourceCodeScanner {

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
    if (fqn !in MATERIAL3_BOTTOM_SHEET_FQCNS) return

    val pkg = context.uastFile?.packageName.orEmpty()
    if (pkg == UI_COMMON_PACKAGE || pkg.startsWith("$UI_COMMON_PACKAGE.")) return

    context.report(
      issue = ISSUE,
      scope = node,
      location = context.getLocation(node),
      message = "Use `com.mataku.scrobscrob.ui_common.component.SunsetBottomSheet` instead of `$fqn`.",
    )
  }

  companion object {
    private val MATERIAL3_BOTTOM_SHEET_FQCNS = setOf(
      "androidx.compose.material3.BottomSheetScaffold",
      "androidx.compose.material3.rememberBottomSheetScaffoldState",
      "androidx.compose.material3.rememberStandardBottomSheetState",
      "androidx.compose.material3.SheetValue",
    )
    private const val UI_COMMON_PACKAGE = "com.mataku.scrobscrob.ui_common"

    val ISSUE: Issue = Issue.create(
      id = "PreferSunsetBottomSheet",
      briefDescription = "Prefer SunsetBottomSheet over material3 BottomSheetScaffold",
      explanation = """
        Outside of `:ui_common`, prefer the `com.mataku.scrobscrob.ui_common.component.SunsetBottomSheet` \
        wrapper over `androidx.compose.material3.BottomSheetScaffold`, \
        `rememberBottomSheetScaffoldState`, `rememberStandardBottomSheetState`, and `SheetValue`. \
        Routing all standard bottom sheets through SunsetBottomSheet keeps feature modules \
        decoupled from material3 versions, fixes the initial sheet value to `PartiallyExpanded`, \
        and centralises sheet container colour wiring on `LocalAppTheme`. Inside `:ui_common` \
        itself the wrapper has to import these material3 APIs, so files in that package are \
        exempt. Suppress with `@Suppress("PreferSunsetBottomSheet")` only when there is a \
        documented reason.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        PreferSunsetBottomSheetDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
