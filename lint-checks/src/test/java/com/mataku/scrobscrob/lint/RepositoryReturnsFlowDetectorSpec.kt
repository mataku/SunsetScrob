package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import io.kotest.core.spec.style.DescribeSpec

class RepositoryReturnsFlowDetectorSpec : DescribeSpec({

  describe("RepositoryReturnsFlowDetector") {

    it("public method returning Flow is allowed") {
      lint()
        .files(
          flowStub,
          kotlin(
            """
              package com.example

              import kotlinx.coroutines.flow.Flow

              interface FooRepository {
                fun items(): Flow<List<String>>
                suspend fun item(id: Int): Flow<String>
              }
            """.trimIndent(),
          ),
        )
        .issues(RepositoryReturnsFlowDetector.ISSUE)
        .run()
        .expectClean()
    }

    it("public method returning a non-Flow type is reported") {
      lint()
        .files(
          flowStub,
          kotlin(
            """
              package com.example

              import kotlinx.coroutines.flow.Flow

              interface FooRepository {
                suspend fun asyncValue(): String?
                fun list(): List<String>
                fun ok(): Flow<Int>
              }
            """.trimIndent(),
          ),
        )
        .issues(RepositoryReturnsFlowDetector.ISSUE)
        .run()
        .expectErrorCount(2)
    }

    it("non-interface *RepositoryImpl is ignored") {
      lint()
        .files(
          flowStub,
          kotlin(
            """
              package com.example

              class FooRepositoryImpl {
                fun something(): String = ""
              }
            """.trimIndent(),
          ),
        )
        .issues(RepositoryReturnsFlowDetector.ISSUE)
        .run()
        .expectClean()
    }

    it("interface not ending with Repository is ignored") {
      lint()
        .files(
          kotlin(
            """
              package com.example

              interface FooService {
                fun something(): String
              }
            """.trimIndent(),
          ),
        )
        .issues(RepositoryReturnsFlowDetector.ISSUE)
        .run()
        .expectClean()
    }
  }
})
