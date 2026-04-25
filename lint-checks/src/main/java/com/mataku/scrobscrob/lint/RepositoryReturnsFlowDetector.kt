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
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiType
import com.intellij.psi.PsiWildcardType
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UastVisibility

class RepositoryReturnsFlowDetector : Detector(), SourceCodeScanner {

  override fun getApplicableUastTypes(): List<Class<out UElement>> =
    listOf(UClass::class.java)

  override fun createUastHandler(context: JavaContext): UElementHandler =
    object : UElementHandler() {
      override fun visitClass(node: UClass) {
        check(context, node)
      }
    }

  private fun check(context: JavaContext, node: UClass) {
    if (!node.isInterface) return
    val name = node.name ?: return
    if (!name.endsWith("Repository")) return

    for (method in node.methods) {
      if (method.isConstructor) continue
      if (method.visibility != UastVisibility.PUBLIC) continue
      if (returnsFlow(method)) continue

      val reportScope: UElement = method
      context.report(
        issue = ISSUE,
        scope = reportScope,
        location = context.getNameLocation(method),
        message = "Repository methods should return `kotlinx.coroutines.flow.Flow<T>`.",
      )
    }
  }

  private fun returnsFlow(method: PsiMethod): Boolean {
    val continuationType = method.parameterList.parameters
      .lastOrNull()
      ?.type
      ?.takeIfContinuation()

    val effective: PsiType? = if (continuationType != null) {
      continuationType.parameters.firstOrNull()?.unwrapWildcard()
    } else {
      method.returnType
    }

    return (effective as? PsiClassType)?.resolve()?.qualifiedName == FLOW_FQCN
  }

  private fun PsiType.takeIfContinuation(): PsiClassType? {
    val classType = this as? PsiClassType ?: return null
    return classType.takeIf { it.resolve()?.qualifiedName == CONTINUATION_FQCN }
  }

  private fun PsiType.unwrapWildcard(): PsiType? = when (this) {
    is PsiWildcardType -> bound
    else -> this
  }

  companion object {
    private const val FLOW_FQCN = "kotlinx.coroutines.flow.Flow"
    private const val CONTINUATION_FQCN = "kotlin.coroutines.Continuation"

    val ISSUE: Issue = Issue.create(
      id = "RepositoryReturnsFlow",
      briefDescription = "Repository public methods must return Flow",
      explanation = """
        Public abstract methods on `*Repository` interfaces should return \
        `kotlinx.coroutines.flow.Flow<T>` so that errors propagate to the call site (the \
        ViewModel maps them to a `UiEvent.Error` via `.catch { }`). Returning a raw value \
        forces the caller to introduce its own error-handling shape and breaks the rest of \
        the codebase's pattern. If you have a legitimate non-Flow accessor, suppress this \
        with `@Suppress("RepositoryReturnsFlow")` and document why.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        RepositoryReturnsFlowDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
