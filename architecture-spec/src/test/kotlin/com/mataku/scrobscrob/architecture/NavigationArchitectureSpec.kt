package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty

class NavigationArchitectureSpec : DescribeSpec({

  val files = Konsist.scopeFromProject().files
    .filterNot { it.path.contains("/architecture-spec/") }
    .filter { it.path.contains("/src/main/") }

  describe("Navigation 3 dependency boundary") {

    it("only :ui_common may import androidx.navigation3") {
      val violations = files
        .filter { it.imports.any { imp -> imp.name.startsWith("androidx.navigation3") } }
        .filterNot { it.path.contains("/ui_common/") }
      withClue(
        ":ui_common is the only module allowed to import androidx.navigation3. Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }

    it("no module may import androidx.navigation (Nav 2)") {
      val violations = files
        .filter { file ->
          file.imports.any { imp ->
            imp.name.startsWith("androidx.navigation.") && !imp.name.startsWith("androidx.navigation3")
          }
        }
      withClue(
        "Navigation 2 has been removed. Use :ui_common's SunsetNav DSL instead. Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }

    it("classes implementing SunsetNavKey must be @Immutable and @Serializable") {
      val violations = Konsist.scopeFromProject().classes()
        .filterNot { it.resideInPath("..architecture-spec..") }
        .filter { c -> c.parents().any { it.name == "SunsetNavKey" } }
        .filter { c ->
          !c.hasAnnotation { it.name == "Immutable" } ||
          !c.hasAnnotation { it.name == "Serializable" }
        }
      withClue(
        "SunsetNavKey implementations must be @Immutable + @Serializable. Offending classes:\n" +
          violations.joinToString("\n") { "${it.name} at ${it.containingFile.path}" },
      ) { violations.shouldBeEmpty() }
    }

    it("NavKey classes live under ui/navigation or ui_common/navigation") {
      val violations = Konsist.scopeFromProject().classes()
        .filterNot { it.resideInPath("..architecture-spec..") }
        .filter { c -> c.parents().any { it.name == "SunsetNavKey" } }
        .filterNot { c ->
          c.resideInPackage("..ui.navigation..") || c.resideInPackage("..ui_common.navigation..")
        }
      withClue(
        "NavKey classes must reside under ui/navigation or ui_common/navigation. Offending classes:\n" +
          violations.joinToString("\n") { "${it.name} at ${it.containingFile.path}" },
      ) { violations.shouldBeEmpty() }
    }

    it("metroViewModel must not be called inside :feature:* navigation files") {
      val violations = files
        .filter { it.path.contains("/feature/") && it.path.contains("/ui/navigation/") }
        .filter { it.text.contains("metroViewModel(") }
      withClue(
        "Use SunsetDestinationScope.viewModelFor(key) instead of metroViewModel inside navigation files. Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }
  }
})
