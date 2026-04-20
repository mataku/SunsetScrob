package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.DescribeSpec

class HiltModuleArchitectureSpec : DescribeSpec({

  val scope = Konsist.scopeFromProduction()

  describe("Hilt module conventions (CLAUDE.md Rule 7)") {

    it("`@Module` classes are named `*Module`") {
      scope.classes()
        .filter { cls -> cls.annotations.any { it.name == "Module" } }
        .assertTrue(
          additionalMessage = "Hilt modules must be named `*Module`. CLAUDE.md Rule 7.",
        ) { it.name.endsWith("Module") }
    }

    it("`@Module` classes declare `@InstallIn`") {
      scope.classes()
        .filter { cls -> cls.annotations.any { it.name == "Module" } }
        .assertTrue(
          additionalMessage = "Hilt modules must declare `@InstallIn(...)`. CLAUDE.md Rule 7.",
        ) { cls -> cls.annotations.any { it.name == "InstallIn" } }
    }
  }
})
