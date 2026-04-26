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

class PreferSunsetTabRowDetector : Detector(), SourceCodeScanner {

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
    if (fqn !in MATERIAL3_TAB_ROW_FQCNS && !fqn.startsWith(MATERIAL3_TAB_ROW_DEFAULTS_PREFIX)) {
      return
    }

    val pkg = context.uastFile?.packageName.orEmpty()
    if (pkg == UI_COMMON_PACKAGE || pkg.startsWith("$UI_COMMON_PACKAGE.")) return

    context.report(
      issue = ISSUE,
      scope = node,
      location = context.getLocation(node),
      message = "Use `com.mataku.scrobscrob.ui_common.component.SunsetTabRow` / `SunsetTab` instead of `$fqn`.",
    )
  }

  companion object {
    private val MATERIAL3_TAB_ROW_FQCNS = setOf(
      "androidx.compose.material3.Tab",
      "androidx.compose.material3.TabRow",
      "androidx.compose.material3.PrimaryTabRow",
      "androidx.compose.material3.SecondaryTabRow",
    )
    private const val MATERIAL3_TAB_ROW_DEFAULTS_PREFIX =
      "androidx.compose.material3.TabRowDefaults"
    private const val UI_COMMON_PACKAGE = "com.mataku.scrobscrob.ui_common"

    val ISSUE: Issue = Issue.create(
      id = "PreferSunsetTabRow",
      briefDescription = "Prefer SunsetTabRow over material3 TabRow / Tab",
      explanation = """
        Outside of `:ui_common`, prefer the `com.mataku.scrobscrob.ui_common.component.SunsetTabRow` and \
        `SunsetTab` wrappers over `androidx.compose.material3.TabRow`, `Tab`, `PrimaryTabRow`, \
        `SecondaryTabRow`, and `TabRowDefaults`. Routing all tabs through SunsetTabRow keeps \
        feature modules decoupled from material3 versions, hides the deprecated `TabRow` API \
        behind `PrimaryTabRow`, and centralises tab indicator wiring in one place. Inside \
        `:ui_common` itself the wrapper has to import material3 `Tab` / `PrimaryTabRow`, so \
        files in that package are exempt. Suppress with `@Suppress("PreferSunsetTabRow")` \
        only when there is a documented reason.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        PreferSunsetTabRowDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
