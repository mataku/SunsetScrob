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

class PreferSunsetTopAppBarDetector : Detector(), SourceCodeScanner {

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
    if (fqn !in MATERIAL3_TOP_APP_BAR_FQCNS) return

    val pkg = context.uastFile?.packageName.orEmpty()
    if (pkg == UI_COMMON_PACKAGE || pkg.startsWith("$UI_COMMON_PACKAGE.")) return

    context.report(
      issue = ISSUE,
      scope = node,
      location = context.getLocation(node),
      message = "Use `com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar` (and `SunsetTopAppBarScrollBehavior`) instead of `$fqn`.",
    )
  }

  companion object {
    private val MATERIAL3_TOP_APP_BAR_FQCNS = setOf(
      "androidx.compose.material3.TopAppBar",
      "androidx.compose.material3.TopAppBarDefaults",
      "androidx.compose.material3.TopAppBarScrollBehavior",
    )
    private const val UI_COMMON_PACKAGE = "com.mataku.scrobscrob.ui_common"

    val ISSUE: Issue = Issue.create(
      id = "PreferSunsetTopAppBar",
      briefDescription = "Prefer SunsetTopAppBar over material3 TopAppBar",
      explanation = """
        Outside of `:ui_common`, prefer the `com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar` \
        wrapper (and `SunsetTopAppBarScrollBehavior` / \
        `rememberSunsetTopAppBarScrollBehavior`) over `androidx.compose.material3.TopAppBar` \
        and friends (`TopAppBarDefaults`, `TopAppBarScrollBehavior`). Routing the top bar \
        through SunsetTopAppBar keeps feature modules decoupled from material3 versions, \
        hides the `ExperimentalMaterial3Api` opt-in, and centralises the default colors. \
        Inside `:ui_common` itself the wrapper has to import material3, so files in that \
        package are exempt. Suppress with `@Suppress("PreferSunsetTopAppBar")` only when \
        there is a documented reason.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        PreferSunsetTopAppBarDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
