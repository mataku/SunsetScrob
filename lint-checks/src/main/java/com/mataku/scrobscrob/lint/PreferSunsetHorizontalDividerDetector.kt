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

class PreferSunsetHorizontalDividerDetector : Detector(), SourceCodeScanner {

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
    if (fqn != MATERIAL3_HORIZONTAL_DIVIDER_FQCN) return

    val pkg = context.uastFile?.packageName.orEmpty()
    if (pkg == UI_COMMON_PACKAGE || pkg.startsWith("$UI_COMMON_PACKAGE.")) return

    context.report(
      issue = ISSUE,
      scope = node,
      location = context.getLocation(node),
      message = "Use `com.mataku.scrobscrob.ui_common.SunsetHorizontalDivider` instead of `androidx.compose.material3.HorizontalDivider`.",
    )
  }

  companion object {
    private const val MATERIAL3_HORIZONTAL_DIVIDER_FQCN = "androidx.compose.material3.HorizontalDivider"
    private const val UI_COMMON_PACKAGE = "com.mataku.scrobscrob.ui_common"

    val ISSUE: Issue = Issue.create(
      id = "PreferSunsetHorizontalDivider",
      briefDescription = "Prefer SunsetHorizontalDivider over material3 HorizontalDivider",
      explanation = """
        Outside of `:ui_common`, prefer the `com.mataku.scrobscrob.ui_common.SunsetHorizontalDivider` \
        wrapper over `androidx.compose.material3.HorizontalDivider`. Routing all dividers through \
        SunsetHorizontalDivider keeps feature modules decoupled from material3 versions and \
        unifies the divider color on `MaterialTheme.colorScheme.onSecondary`. Inside `:ui_common` \
        itself the wrapper has to import material3 `HorizontalDivider`, so files in that package \
        are exempt. Suppress with `@Suppress("PreferSunsetHorizontalDivider")` only when there is \
        a documented reason.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        PreferSunsetHorizontalDividerDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
