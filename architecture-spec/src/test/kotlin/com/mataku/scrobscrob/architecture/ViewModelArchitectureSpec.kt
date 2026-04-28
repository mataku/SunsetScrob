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

    it("AndroidViewModel-derived ViewModels pin the bound type via `binding = binding<ViewModel>()`") {
      scope.classes()
        .withNameEndingWith("ViewModel")
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .filter { vm -> vm.hasParent { parent -> parent.name == "AndroidViewModel" } }
        .assertTrue(
          additionalMessage = "ViewModels extending AndroidViewModel must declare " +
            "`@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())`. Without " +
            "the `binding` argument Metro contributes them into a `Map<_, AndroidViewModel>` " +
            "multibinding that `MetroViewModelFactory` never reads, so the VM silently drops " +
            "out of the graph and `metroViewModel<T>()` fails at runtime. CLAUDE.md Rule 3.",
        ) { vm ->
          val annotation = vm.annotations.firstOrNull { it.name == "ContributesIntoMap" }
            ?: return@assertTrue false
          annotation.arguments.any { it.name == "binding" }
        }
    }

    it("`@AssistedInject` ViewModels expose a Factory : ViewModelAssistedFactory with the required annotations") {
      scope.classes()
        .withNameEndingWith("ViewModel")
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .filter { vm -> vm.annotations.any { it.name == "AssistedInject" } }
        .assertTrue(
          additionalMessage = "AssistedInject ViewModels must declare a nested " +
            "`Factory : ViewModelAssistedFactory` annotated with `@AssistedFactory`, " +
            "`@ViewModelAssistedFactoryKey(<this>::class)`, and " +
            "`@ContributesIntoMap(AppScope::class)`. Missing any of the three causes the " +
            "Factory to drop out of the assisted-factory multibinding and " +
            "`metroViewModel<T>()` fails at runtime. CLAUDE.md Rule 3.",
        ) { vm ->
          val factory = vm.interfaces(includeNested = true)
            .firstOrNull { intf ->
              intf.hasParent { parent -> parent.name == "ViewModelAssistedFactory" }
            }
            ?: return@assertTrue false
          val annotationNames = factory.annotations.map { it.name }
          "AssistedFactory" in annotationNames &&
            "ViewModelAssistedFactoryKey" in annotationNames &&
            "ContributesIntoMap" in annotationNames
        }
    }
  }
})
