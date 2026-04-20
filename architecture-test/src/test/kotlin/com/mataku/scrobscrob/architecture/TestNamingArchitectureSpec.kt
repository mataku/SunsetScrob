package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty

class TestNamingArchitectureSpec : DescribeSpec({

  val files = Konsist.scopeFromTest().files
    .filterNot { it.path.contains("/architecture-test/") }

  describe("Test naming conventions (CLAUDE.md Rule 8)") {

    it("Kotest spec files are named `*Spec.kt`") {
      val violations = files
        .filter { file ->
          file.classes().any { cls ->
            cls.parents().any { parent -> parent.name.endsWith("Spec") }
          }
        }
        .filterNot { it.name.endsWith("Spec") }

      withClue(
        "Files defining a Kotest `*Spec` must be named `*Spec.kt`. CLAUDE.md Rule 8. Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }

    it("Roborazzi screenshot files end with `Test.kt`") {
      val violations = files
        .filter { file ->
          file.classes().any { cls ->
            cls.annotations.any { it.name == "GraphicsMode" }
          }
        }
        .filterNot { it.name.endsWith("Test") }

      withClue(
        "Roborazzi screenshot test files (annotated `@GraphicsMode`) must end with `Test.kt` " +
          "(typically `*ScreenTest.kt` for screens, `*Test.kt` for components). CLAUDE.md Rule 8. " +
          "Offending files:\n" + violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }
  }
})
