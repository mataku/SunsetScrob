package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import io.kotest.core.spec.style.DescribeSpec

class PreviewComposableVisibilityDetectorSpec : DescribeSpec({

  describe("PreviewComposableVisibilityDetector") {

    it("private @Preview composable is allowed") {
      lint()
        .files(
          composableStub,
          previewStub,
          kotlin(
            """
              package com.example

              import androidx.compose.runtime.Composable
              import androidx.compose.ui.tooling.preview.Preview

              @Preview
              @Composable
              private fun PreviewFoo() {}
            """.trimIndent(),
          ),
        )
        .issues(PreviewComposableVisibilityDetector.ISSUE)
        .run()
        .expectClean()
    }

    it("public @Preview composable (no modifier) is reported") {
      lint()
        .files(
          composableStub,
          previewStub,
          kotlin(
            """
              package com.example

              import androidx.compose.runtime.Composable
              import androidx.compose.ui.tooling.preview.Preview

              @Preview
              @Composable
              fun PreviewFoo() {}
            """.trimIndent(),
          ),
        )
        .issues(PreviewComposableVisibilityDetector.ISSUE)
        .run()
        .expectErrorCount(1)
    }

    it("internal @Preview composable is reported") {
      lint()
        .files(
          composableStub,
          previewStub,
          kotlin(
            """
              package com.example

              import androidx.compose.runtime.Composable
              import androidx.compose.ui.tooling.preview.Preview

              @Preview
              @Composable
              internal fun PreviewFoo() {}
            """.trimIndent(),
          ),
        )
        .issues(PreviewComposableVisibilityDetector.ISSUE)
        .run()
        .expectErrorCount(1)
    }

    it("@Preview without @Composable is ignored") {
      lint()
        .files(
          previewStub,
          kotlin(
            """
              package com.example

              import androidx.compose.ui.tooling.preview.Preview

              @Preview
              fun notAComposable() {}
            """.trimIndent(),
          ),
        )
        .issues(PreviewComposableVisibilityDetector.ISSUE)
        .run()
        .expectClean()
    }

    it("internal @Preview composable annotated @ShowkaseComposable is allowed") {
      lint()
        .files(
          composableStub,
          previewStub,
          showkaseComposableStub,
          kotlin(
            """
              package com.example

              import androidx.compose.runtime.Composable
              import androidx.compose.ui.tooling.preview.Preview
              import com.airbnb.android.showkase.annotation.ShowkaseComposable

              @Preview
              @ShowkaseComposable(name = "Foo", group = "Design system")
              @Composable
              internal fun PreviewFoo() {}
            """.trimIndent(),
          ),
        )
        .issues(PreviewComposableVisibilityDetector.ISSUE)
        .run()
        .expectClean()
    }

    it("public @Preview composable annotated @ShowkaseComposable is allowed") {
      lint()
        .files(
          composableStub,
          previewStub,
          showkaseComposableStub,
          kotlin(
            """
              package com.example

              import androidx.compose.runtime.Composable
              import androidx.compose.ui.tooling.preview.Preview
              import com.airbnb.android.showkase.annotation.ShowkaseComposable

              @Preview
              @ShowkaseComposable(name = "Foo", group = "Design system")
              @Composable
              fun PreviewFoo() {}
            """.trimIndent(),
          ),
        )
        .issues(PreviewComposableVisibilityDetector.ISSUE)
        .run()
        .expectClean()
    }
  }
})
