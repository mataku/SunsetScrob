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
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement

class UiStateMustBeStateFlowDetector : Detector(), SourceCodeScanner {

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
    if (!name.endsWith("ViewModel")) return

    for (field in node.fields) {
      if (field.name != "uiState") continue

      val classType = field.type as? PsiClassType ?: continue
      val fqn = classType.resolve()?.qualifiedName ?: continue
      if (fqn == STATEFLOW_FQCN) continue

      context.report(
        issue = ISSUE,
        scope = field as UElement,
        location = context.getNameLocation(field),
        message = "`uiState` must be exposed as `StateFlow<...>` (got `$fqn`).",
      )
    }
  }

  companion object {
    private const val STATEFLOW_FQCN = "kotlinx.coroutines.flow.StateFlow"

    val ISSUE: Issue = Issue.create(
      id = "UiStateMustBeStateFlow",
      briefDescription = "ViewModel `uiState` must be `StateFlow<...>`",
      explanation = """
        Public `uiState` properties on ViewModels must be exposed as \
        `kotlinx.coroutines.flow.StateFlow<...>`. Use the Kotlin 2.3 explicit backing \
        field pattern: `val uiState: StateFlow<X> field = MutableStateFlow(...)`. \
        Do not expose `MutableStateFlow` or Compose `mutableStateOf` — callers must \
        not be able to mutate the state.
      """.trimIndent(),
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(
        UiStateMustBeStateFlowDetector::class.java,
        Scope.JAVA_FILE_SCOPE,
      ),
    )
  }
}
