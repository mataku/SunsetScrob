package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.DescribeSpec

class ViewModelArchitectureSpec : DescribeSpec({

  val scope = Konsist.scopeFromProduction()

  describe("ViewModel conventions (CLAUDE.md Rule 3)") {

    it("classes ending with ViewModel are annotated with @HiltViewModel") {
      scope.classes()
        .withNameEndingWith("ViewModel")
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .assertTrue(
          additionalMessage = "All ViewModels must be annotated with @HiltViewModel. CLAUDE.md Rule 3.",
        ) { vm -> vm.annotations.any { it.name == "HiltViewModel" } }
    }

    it("classes ending with ViewModel extend ViewModel or AndroidViewModel") {
      scope.classes()
        .withNameEndingWith("ViewModel")
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .assertTrue(
          additionalMessage = "ViewModels must extend ViewModel or AndroidViewModel. CLAUDE.md Rule 3.",
        ) { vm ->
          vm.hasParent { parent ->
            parent.name == "ViewModel" || parent.name == "AndroidViewModel"
          }
        }
    }

    it("ViewModels are declared `internal`") {
      // Hub exception: these VMs are consumed by :feature:home via
      // hiltViewModel<T>() in HomeScreen's tabs, so their type must be
      // visible across module boundaries. Mirrors Rule 1's feature-to-feature exception.
      val hubConsumedVms = setOf(
        "TopAlbumsViewModel",
        "TopArtistsViewModel",
        "ScrobbleViewModel",
      )
      scope.classes()
        .withNameEndingWith("ViewModel")
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .filterNot { it.name in hubConsumedVms }
        .assertTrue(
          additionalMessage = "ViewModels are not shared across modules (except tabs embedded by :feature:home); declare them `internal`. CLAUDE.md Rule 3.",
        ) { it.hasModifier(KoModifier.INTERNAL) }
    }
  }
})
