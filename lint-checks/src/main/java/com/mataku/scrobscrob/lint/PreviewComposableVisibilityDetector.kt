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
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.UastVisibility

class PreviewComposableVisibilityDetector : Detector(), SourceCodeScanner {

  override fun getApplicableUastTypes(): List<Class<out UElement>> =
    listOf(UMethod::class.java)

  override fun createUastHandler(context: JavaContext): UElementHandler =
    object : UElementHandler() {
      override fun visitMethod(node: UMethod) {
        check(context, node)
      }
    }

  private fun check(context: JavaContext, node: UMethod) {
    val annotations = node.uAnnotations
    val hasPreview = annotations.any { it.qualifiedName == PREVIEW_ANNOTATION }
    if (!hasPreview) return

    val hasComposable = annotations.any { it.qualifiedName == COMPOSABLE_ANNOTATION }
    if (!hasComposable) return

    // `@ShowkaseComposable` previews must be visible to the Showkase KSP processor so that
    // they are picked up into the in-app component catalog. The processor is configured with
    // `skipPrivatePreviews=true` in `ui_common/build.gradle.kts`, which drops `private`
    // previews from the catalog. As a result these previews are required to be `internal`
    // (or `public`) — see https://github.com/airbnb/Showkase. Skip the visibility rule for
    // them so the two requirements do not contradict each other.
    val hasShowkase = annotations.any { it.qualifiedName == SHOWKASE_ANNOTATION }
    if (hasShowkase) return

    if (node.visibility == UastVisibility.PRIVATE) return

    context.report(
      issue = ISSUE,
      scope = node as UElement,
      location = context.getNameLocation(node),
      message = "`@Preview` composable should be `private`.",
    )
  }

  companion object {
    private const val PREVIEW_ANNOTATION = "androidx.compose.ui.tooling.preview.Preview"
    private const val COMPOSABLE_ANNOTATION = "androidx.compose.runtime.Composable"
    private const val SHOWKASE_ANNOTATION = "com.airbnb.android.showkase.annotation.ShowkaseComposable"

    val ISSUE: Issue = Issue.create(
      id = "PreviewNotPrivate",
      briefDescription = "`@Preview` composable must be private",
      explanation = """
        Composables annotated with `@Preview` should not leak into the module's public API. \
        Mark the function `private` so it stays a development-only helper. If you genuinely \
        need a non-private preview (for example a shared preview reused across modules), \
        suppress this with `@Suppress("PreviewNotPrivate")`.

        Exception — `@ShowkaseComposable`: previews additionally annotated with \
        `@ShowkaseComposable` are skipped by this check. Showkase's KSP processor is \
        configured with `skipPrivatePreviews=true` in `ui_common/build.gradle.kts`, which \
        means a `private` preview never reaches the in-app component catalog. Such previews \
        therefore need to be `internal` (or `public`) so the processor can see them. See \
        https://github.com/airbnb/Showkase for details.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        PreviewComposableVisibilityDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
