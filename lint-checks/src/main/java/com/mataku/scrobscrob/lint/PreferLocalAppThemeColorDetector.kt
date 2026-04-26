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
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression

class PreferLocalAppThemeColorDetector : Detector(), SourceCodeScanner {

  override fun getApplicableUastTypes(): List<Class<out UElement>> =
    listOf(UQualifiedReferenceExpression::class.java)

  override fun createUastHandler(context: JavaContext): UElementHandler =
    object : UElementHandler() {
      override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
        check(context, node)
      }
    }

  private fun check(context: JavaContext, node: UQualifiedReferenceExpression) {
    val selector = node.selector as? USimpleNameReferenceExpression ?: return
    if (selector.identifier != COLOR_SCHEME) return

    val containingFqn = when (val resolved = selector.resolve()) {
      is PsiMethod -> resolved.containingClass?.qualifiedName
      is PsiField -> resolved.containingClass?.qualifiedName
      else -> null
    }
    if (containingFqn != MATERIAL_THEME_FQCN) return

    val pkg = context.uastFile?.packageName.orEmpty()
    if (pkg == UI_COMMON_PACKAGE || pkg.startsWith("$UI_COMMON_PACKAGE.")) return

    context.report(
      issue = ISSUE,
      scope = node,
      location = context.getLocation(node),
      message = "Use `LocalAppTheme.current.<color>Color()` from `:ui_common` instead of `MaterialTheme.colorScheme`.",
    )
  }

  companion object {
    private const val COLOR_SCHEME = "colorScheme"
    private const val MATERIAL_THEME_FQCN = "androidx.compose.material3.MaterialTheme"
    private const val UI_COMMON_PACKAGE = "com.mataku.scrobscrob.ui_common"

    val ISSUE: Issue = Issue.create(
      id = "PreferLocalAppThemeColor",
      briefDescription = "Prefer LocalAppTheme color extensions over MaterialTheme.colorScheme",
      explanation = """
        Outside of `:ui_common`, prefer `LocalAppTheme.current.backgroundColor()`, \
        `onSurfaceColor()`, and the other `:ui_common` color extensions over reading colors \
        from `MaterialTheme.colorScheme`. Routing color access through `LocalAppTheme` keeps \
        feature modules decoupled from `androidx.compose.material3`, lets future material \
        upgrades or theme replacements land in one place, and matches the existing \
        `accentColor()` / `backgroundColor()` pattern. Inside `:ui_common` itself the wrapper \
        layer is allowed to read material3 colors directly, so files in that package are \
        exempt. If you genuinely need `MaterialTheme.colorScheme` elsewhere (a transitional \
        shim, deeply customised material API, etc.), suppress this with \
        `@Suppress("PreferLocalAppThemeColor")` and document why.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        PreferLocalAppThemeColorDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
