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

class PreferSunsetListDetailPaneScaffoldDetector : Detector(), SourceCodeScanner {

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
    if (!fqn.startsWith(MATERIAL3_ADAPTIVE_PACKAGE_PREFIX)) return

    val pkg = context.uastFile?.packageName.orEmpty()
    if (pkg == UI_COMMON_PACKAGE || pkg.startsWith("$UI_COMMON_PACKAGE.")) return

    context.report(
      issue = ISSUE,
      scope = node,
      location = context.getLocation(node),
      message = "Use `com.mataku.scrobscrob.ui_common.component.designsystem.SunsetListDetailScaffold` instead of `$fqn`.",
    )
  }

  companion object {
    private const val MATERIAL3_ADAPTIVE_PACKAGE_PREFIX = "androidx.compose.material3.adaptive"
    private const val UI_COMMON_PACKAGE = "com.mataku.scrobscrob.ui_common"

    val ISSUE: Issue = Issue.create(
      id = "PreferSunsetListDetailPaneScaffold",
      briefDescription = "Prefer SunsetListDetailScaffold over material3 adaptive APIs",
      explanation = """
        Outside of `:ui_common`, prefer the `com.mataku.scrobscrob.ui_common.component.designsystem.SunsetListDetailScaffold` \
        wrapper over any `androidx.compose.material3.adaptive.*` API. \
        Routing list-detail scaffolds through SunsetListDetailScaffold keeps feature modules \
        decoupled from the material3-adaptive library version, hides the experimental \
        `ThreePaneScaffoldNavigator` / `ListDetailPaneScaffoldRole` types, and centralises \
        directive / adapt-strategy choices in one place. Inside `:ui_common` itself the wrapper \
        has to import these material3-adaptive APIs, so files in that package are exempt. \
        Suppress with `@Suppress("PreferSunsetListDetailPaneScaffold")` only when there is a \
        documented reason.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        PreferSunsetListDetailPaneScaffoldDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
