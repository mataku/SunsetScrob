package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import io.kotest.core.spec.style.DescribeSpec

class UiStateMustBeImmutableDetectorSpec : DescribeSpec({

  describe("UiStateMustBeImmutableDetector") {

    it("UiState nested in a ViewModel with @Immutable is allowed") {
      lint()
        .files(
          immutableStub,
          viewModelStub,
          kotlin(
            """
              package com.example

              import androidx.compose.runtime.Immutable
              import androidx.lifecycle.ViewModel

              class FooViewModel : ViewModel() {
                @Immutable
                data class FooUiState(val count: Int = 0)
              }
            """.trimIndent(),
          ),
        )
        .issues(UiStateMustBeImmutableDetector.ISSUE)
        .run()
        .expectClean()
    }

    it("UiState nested in a ViewModel without @Immutable is reported") {
      lint()
        .files(
          immutableStub,
          viewModelStub,
          kotlin(
            """
              package com.example

              import androidx.lifecycle.ViewModel

              class FooViewModel : ViewModel() {
                data class FooUiState(val count: Int = 0)
              }
            """.trimIndent(),
          ),
        )
        .issues(UiStateMustBeImmutableDetector.ISSUE)
        .run()
        .expectErrorCount(1)
    }

    it("top-level UiState class (not nested in a ViewModel) is ignored") {
      lint()
        .files(
          immutableStub,
          kotlin(
            """
              package com.example

              data class FooUiState(val count: Int = 0)
            """.trimIndent(),
          ),
        )
        .issues(UiStateMustBeImmutableDetector.ISSUE)
        .run()
        .expectClean()
    }

    it("UiState nested in a non-ViewModel class is ignored") {
      lint()
        .files(
          immutableStub,
          kotlin(
            """
              package com.example

              class FooHelper {
                data class FooUiState(val count: Int = 0)
              }
            """.trimIndent(),
          ),
        )
        .issues(UiStateMustBeImmutableDetector.ISSUE)
        .run()
        .expectClean()
    }
  }
})
