package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import io.kotest.core.spec.style.DescribeSpec

class UiStateMustBeStateFlowDetectorSpec : DescribeSpec({

  describe("UiStateMustBeStateFlowDetector") {

    it("uiState typed StateFlow via the explicit backing field is allowed") {
      lint()
        .files(
          flowStub,
          stateFlowStub,
          viewModelStub,
          kotlin(
            """
              package com.example

              import androidx.lifecycle.ViewModel
              import kotlinx.coroutines.flow.MutableStateFlow
              import kotlinx.coroutines.flow.StateFlow

              class FooViewModel : ViewModel() {
                val uiState: StateFlow<FooUiState>
                  field = MutableStateFlow(FooUiState())
              }

              data class FooUiState(val count: Int = 0)
            """.trimIndent(),
          ),
        )
        .issues(UiStateMustBeStateFlowDetector.ISSUE)
        .run()
        .expectClean()
    }

    it("uiState typed MutableStateFlow is reported") {
      lint()
        .files(
          flowStub,
          stateFlowStub,
          viewModelStub,
          kotlin(
            """
              package com.example

              import androidx.lifecycle.ViewModel
              import kotlinx.coroutines.flow.MutableStateFlow

              class FooViewModel : ViewModel() {
                var uiState = MutableStateFlow(FooUiState())
                  private set
              }

              data class FooUiState(val count: Int = 0)
            """.trimIndent(),
          ),
        )
        .issues(UiStateMustBeStateFlowDetector.ISSUE)
        .run()
        .expectErrorCount(1)
    }

    it("classes not ending with ViewModel are ignored") {
      lint()
        .files(
          flowStub,
          stateFlowStub,
          kotlin(
            """
              package com.example

              import kotlinx.coroutines.flow.MutableStateFlow

              class FooHelper {
                var uiState = MutableStateFlow(0)
              }
            """.trimIndent(),
          ),
        )
        .issues(UiStateMustBeStateFlowDetector.ISSUE)
        .run()
        .expectClean()
    }

    it("ViewModel without a uiState property is ignored") {
      lint()
        .files(
          viewModelStub,
          kotlin(
            """
              package com.example

              import androidx.lifecycle.ViewModel

              class FooViewModel : ViewModel() {
                val title: String = ""
              }
            """.trimIndent(),
          ),
        )
        .issues(UiStateMustBeStateFlowDetector.ISSUE)
        .run()
        .expectClean()
    }
  }
})
