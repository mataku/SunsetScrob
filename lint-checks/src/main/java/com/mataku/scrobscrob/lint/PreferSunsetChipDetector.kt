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

class PreferSunsetChipDetector : Detector(), SourceCodeScanner {

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
    if (fqn !in MATERIAL3_CHIP_FQCNS &&
      !fqn.startsWith(MATERIAL3_SUGGESTION_CHIP_DEFAULTS_PREFIX)
    ) {
      return
    }

    val pkg = context.uastFile?.packageName.orEmpty()
    if (pkg == UI_COMMON_PACKAGE || pkg.startsWith("$UI_COMMON_PACKAGE.")) return

    context.report(
      issue = ISSUE,
      scope = node,
      location = context.getLocation(node),
      message = "Use `com.mataku.scrobscrob.ui_common.component.SunsetChip` instead of `$fqn`.",
    )
  }

  companion object {
    private val MATERIAL3_CHIP_FQCNS = setOf(
      "androidx.compose.material3.SuggestionChip",
      "androidx.compose.material3.ChipColors",
    )
    private const val MATERIAL3_SUGGESTION_CHIP_DEFAULTS_PREFIX =
      "androidx.compose.material3.SuggestionChipDefaults"
    private const val UI_COMMON_PACKAGE = "com.mataku.scrobscrob.ui_common"

    val ISSUE: Issue = Issue.create(
      id = "PreferSunsetChip",
      briefDescription = "Prefer SunsetChip over material3 SuggestionChip",
      explanation = """
        Outside of `:ui_common`, prefer the `com.mataku.scrobscrob.ui_common.component.SunsetChip` \
        wrapper over `androidx.compose.material3.SuggestionChip`, `SuggestionChipDefaults`, \
        and `ChipColors`. Routing all chips through SunsetChip keeps feature modules decoupled \
        from material3 versions and applies the project's standard shape and border. Inside \
        `:ui_common` itself the wrapper has to import material3 `SuggestionChip`, so files in \
        that package are exempt. Suppress with `@Suppress("PreferSunsetChip")` only when there \
        is a documented reason.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        PreferSunsetChipDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
