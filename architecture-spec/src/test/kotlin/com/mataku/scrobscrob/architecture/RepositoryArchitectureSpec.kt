package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.DescribeSpec

class RepositoryArchitectureSpec : DescribeSpec({

  val scope = Konsist.scopeFromProduction()

  describe("Repository conventions (.claude/rules/repository.md)") {

    it("`*Repository` interfaces declare methods returning `Flow<T>`") {
      scope.interfaces()
        .filter { it.name.endsWith("Repository") }
        .filter { it.resideInPackage("com.mataku.scrobscrob.data.repository..") }
        .flatMap { it.functions() }
        .assertTrue(
          additionalMessage = "Repository methods should return `kotlinx.coroutines.flow.Flow<T>`. .claude/rules/repository.md.",
        ) { function -> function.returnType?.name?.startsWith("Flow") == true }
    }
  }
})
