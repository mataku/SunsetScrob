package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.DescribeSpec

class BindingContainerArchitectureSpec : DescribeSpec({

  val scope = Konsist.scopeFromProduction()

  describe("Metro binding container conventions (AGENTS.md Rule 7)") {

    it("interfaces annotated with `@ContributesTo` are named `*Module`") {
      scope.interfaces()
        .filter { intf -> intf.annotations.any { it.name == "ContributesTo" } }
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .filterNot { it.name.endsWith("Graph") }
        .assertTrue(
          additionalMessage = "Metro binding containers must be named `*Module`. AGENTS.md Rule 7.",
        ) { it.name.endsWith("Module") }
    }

    it("interfaces named `*Module` declare `@ContributesTo(AppScope::class)`") {
      scope.interfaces()
        .filter { it.name.endsWith("Module") }
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .assertTrue(
          additionalMessage = "Metro binding containers must declare `@ContributesTo(AppScope::class)`. AGENTS.md Rule 7.",
        ) { cls -> cls.annotations.any { it.name == "ContributesTo" } }
    }

    it("interfaces named `*Module` are annotated `@BindingContainer`") {
      scope.interfaces()
        .filter { it.name.endsWith("Module") }
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .assertTrue(
          additionalMessage = "Metro binding containers must be annotated `@BindingContainer`. AGENTS.md Rule 7.",
        ) { cls -> cls.annotations.any { it.name == "BindingContainer" } }
    }
  }
})
