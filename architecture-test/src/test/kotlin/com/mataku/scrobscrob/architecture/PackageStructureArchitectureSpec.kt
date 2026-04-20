package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.DescribeSpec

class PackageStructureArchitectureSpec : DescribeSpec({

  val scope = Konsist.scopeFromProduction()

  describe("Package structure rules (CLAUDE.md Rule 2)") {

    it("ViewModels reside in a `...ui.viewmodel` package") {
      scope.classes()
        .withNameEndingWith("ViewModel")
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .assertTrue(
          additionalMessage = "ViewModels must live in `...ui.viewmodel`. CLAUDE.md Rule 2 & 3.",
        ) { it.resideInPackage("..ui.viewmodel..") }
    }

    it("Hilt modules reside in a `...di` package") {
      scope.classes()
        .filter { cls -> cls.annotations.any { it.name == "Module" } }
        .assertTrue(
          additionalMessage = "Hilt modules must live in `...di`. CLAUDE.md Rule 2 & 7.",
        ) { it.resideInPackage("..di..") }
    }
  }
})
