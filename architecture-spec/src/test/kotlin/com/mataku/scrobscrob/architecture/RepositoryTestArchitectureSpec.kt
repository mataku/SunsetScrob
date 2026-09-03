package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty

class RepositoryTestArchitectureSpec : DescribeSpec({
  describe(":data:repository test sources must not depend on Ktor") {
    it("no *Spec.kt under data/repository tests imports io.ktor.*") {
      val violations = Konsist.scopeFromTest().files
        .filter { it.path.contains("/data/repository/") && it.path.isTestSourcePath() }
        .filter { it.name.endsWith("Spec.kt") }
        .filter { file -> file.imports.any { it.name.startsWith("io.ktor.") } }

      withClue(
        "Repository specs must mock LastFmService rather than wire up Ktor. Move MockEngine " +
          "wiring into a `:data:api` Endpoint spec. See " +
          "docs/specs/2026-04-30-repository-spec-mockengine-extraction-design.md. " +
          "Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) {
        violations.shouldBeEmpty()
      }
    }
  }
})
