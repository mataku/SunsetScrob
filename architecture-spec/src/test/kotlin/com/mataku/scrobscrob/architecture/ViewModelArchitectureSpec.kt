package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.DescribeSpec

class ViewModelArchitectureSpec : DescribeSpec({

  val scope = Konsist.scopeFromProduction()

  describe("ViewModel conventions (CLAUDE.md Rule 3)") {

    it("classes ending with ViewModel are annotated with Metro ViewModel or AssistedInject conventions") {
      scope.classes()
        .withNameEndingWith("ViewModel")
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .assertTrue(
          additionalMessage = "All ViewModels must either be annotated with @Inject + @ViewModelKey + @ContributesIntoMap(AppScope::class) (constructor-injected) or with @AssistedInject and expose an inner Factory : ViewModelAssistedFactory annotated with @AssistedFactory + @ViewModelAssistedFactoryKey + @ContributesIntoMap(AppScope::class). CLAUDE.md Rule 3.",
        ) { vm ->
          val names = vm.annotations.map { it.name }
          val plain = "Inject" in names && "ViewModelKey" in names && "ContributesIntoMap" in names
          val assisted = "AssistedInject" in names
          plain || assisted
        }
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
  }
})
