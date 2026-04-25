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
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement

class UiStateMustBeImmutableDetector : Detector(), SourceCodeScanner {

  override fun getApplicableUastTypes(): List<Class<out UElement>> =
    listOf(UClass::class.java)

  override fun createUastHandler(context: JavaContext): UElementHandler =
    object : UElementHandler() {
      override fun visitClass(node: UClass) {
        check(context, node)
      }
    }

  private fun check(context: JavaContext, node: UClass) {
    val name = node.name ?: return
    if (!name.endsWith("UiState")) return

    val outer = node.uastParent as? UClass ?: return
    if (outer.name?.endsWith("ViewModel") != true) return

    val hasImmutable = node.uAnnotations.any {
      it.qualifiedName == IMMUTABLE_ANNOTATION
    }
    if (hasImmutable) return

    context.report(
      issue = ISSUE,
      scope = node as UElement,
      location = context.getNameLocation(node),
      message = "`$name` must be annotated `@Immutable`.",
    )
  }

  companion object {
    private const val IMMUTABLE_ANNOTATION = "androidx.compose.runtime.Immutable"

    val ISSUE: Issue = Issue.create(
      id = "UiStateMustBeImmutable",
      briefDescription = "`*UiState` classes nested in a ViewModel must be `@Immutable`",
      explanation = """
        Classes ending in `UiState` that are nested inside a `*ViewModel` (the type \
        carried by `StateFlow<...>`) must be annotated `@Immutable` so the Compose \
        compiler can skip recompositions when the reference is structurally equal.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        UiStateMustBeImmutableDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
