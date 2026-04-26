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

class PreferSunsetSnackbarHostDetector : Detector(), SourceCodeScanner {

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
    if (fqn !in MATERIAL3_SNACKBAR_HOST_FQCNS) return

    val pkg = context.uastFile?.packageName.orEmpty()
    if (pkg == UI_COMMON_PACKAGE || pkg.startsWith("$UI_COMMON_PACKAGE.")) return

    context.report(
      issue = ISSUE,
      scope = node,
      location = context.getLocation(node),
      message = "Use `com.mataku.scrobscrob.ui_common.component.SunsetSnackbarHost` (and `SunsetSnackbarHostState`) instead of `$fqn`.",
    )
  }

  companion object {
    private val MATERIAL3_SNACKBAR_HOST_FQCNS = setOf(
      "androidx.compose.material3.SnackbarHost",
      "androidx.compose.material3.SnackbarHostState",
    )
    private const val UI_COMMON_PACKAGE = "com.mataku.scrobscrob.ui_common"

    val ISSUE: Issue = Issue.create(
      id = "PreferSunsetSnackbarHost",
      briefDescription = "Prefer SunsetSnackbarHost over material3 SnackbarHost",
      explanation = """
        Outside of `:ui_common`, prefer the \
        `com.mataku.scrobscrob.ui_common.component.SunsetSnackbarHost` wrapper (and \
        `SunsetSnackbarHostState`) over `androidx.compose.material3.SnackbarHost` and \
        `SnackbarHostState`. Feature modules should reach the host via \
        `LocalSnackbarHostState.current` and call `showSnackbar(...)` on the wrapper rather \
        than importing material3 directly. Inside `:ui_common` itself the wrapper has to \
        import material3, so files in that package are exempt. Suppress with \
        `@Suppress("PreferSunsetSnackbarHost")` only when there is a documented reason.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        PreferSunsetSnackbarHostDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
