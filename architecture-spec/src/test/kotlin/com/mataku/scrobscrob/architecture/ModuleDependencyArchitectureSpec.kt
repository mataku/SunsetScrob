package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty

private const val ROOT = "com.mataku.scrobscrob."
private val FEATURE_SUBPACKAGES = listOf(
  "home", "album", "artist", "scrobble", "auth", "account", "chart",
)

class ModuleDependencyArchitectureSpec : DescribeSpec({

  val files = Konsist.scopeFromProject().files
    .filterNot { it.path.contains("/architecture-spec/") }
    .filter { it.path.contains("/src/main/") }

  describe("Module dependency rules (CLAUDE.md Rule 1)") {

    it(":core must not depend on any other project module") {
      val violations = files
        .filter { it.path.contains("/core/src/") }
        .filter { file ->
          file.imports.any { imp ->
            imp.name.startsWith(ROOT) && !imp.name.startsWith("${ROOT}core.")
          }
        }
      withClue(
        ":core is the pure layer. Violates CLAUDE.md Rule 1. Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }

    it(":ui_common may depend only on :core") {
      val violations = files
        .filter { it.path.contains("/ui_common/src/") }
        .filter { file ->
          file.imports.any { imp ->
            imp.name.startsWith(ROOT) &&
              !imp.name.startsWith("${ROOT}core.") &&
              !imp.name.startsWith("${ROOT}ui_common.")
          }
        }
      withClue(
        ":ui_common may depend on :core only. Violates CLAUDE.md Rule 1. Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }

    it(":data:api and :data:db must not cross-import") {
      val apiViolations = files
        .filter { it.path.contains("/data/api/src/") }
        .filter { file -> file.imports.any { it.name.startsWith("${ROOT}data.db.") } }
      val dbViolations = files
        .filter { it.path.contains("/data/db/src/") }
        .filter { file -> file.imports.any { it.name.startsWith("${ROOT}data.api.") } }
      withClue(
        ":data:api and :data:db are peers and must not cross-import. Violates CLAUDE.md Rule 1. Offending files:\n" +
          (apiViolations + dbViolations).joinToString("\n") { it.path },
      ) { (apiViolations + dbViolations).shouldBeEmpty() }
    }

    it(":data:repository must not depend on UI layer (ui_common or any feature)") {
      val forbidden = listOf("${ROOT}ui_common.") +
        FEATURE_SUBPACKAGES.map { "${ROOT}$it." }
      val violations = files
        .filter { it.path.contains("/data/repository/src/") }
        .filter { file ->
          file.imports.any { imp -> forbidden.any { imp.name.startsWith(it) } }
        }
      withClue(
        ":data:repository sits below the UI layer. Violates CLAUDE.md Rule 1. Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }

    it("feature modules must not depend on :data:api directly") {
      val violations = files
        .filter { it.path.contains("/feature/") && it.path.contains("/src/") }
        .filter { file -> file.imports.any { it.name.startsWith("${ROOT}data.api.") } }
      withClue(
        "Features must go through :data:repository, not :data:api. Violates CLAUDE.md Rule 1. Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }

    it("feature modules must not depend on :data:db directly") {
      val violations = files
        .filter { it.path.contains("/feature/") && it.path.contains("/src/") }
        .filter { file -> file.imports.any { it.name.startsWith("${ROOT}data.db.") } }
      withClue(
        "Features must go through :data:repository, not :data:db. Violates CLAUDE.md Rule 1. Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }

    it("only :feature:home may depend on another feature module") {
      val violations = files
        .filter { it.path.contains("/feature/") && it.path.contains("/src/") }
        .filterNot { it.path.contains("/feature/home/") }
        .mapNotNull { file ->
          val selfSub = FEATURE_SUBPACKAGES.firstOrNull { sub ->
            file.path.contains("/feature/$sub/") ||
              (sub == "chart" && file.path.contains("/feature/discover/"))
          }
          val crossImports = file.imports.filter { imp ->
            FEATURE_SUBPACKAGES.any { other ->
              other != selfSub && imp.name.startsWith("${ROOT}$other.")
            }
          }
          if (crossImports.isEmpty()) null else file to crossImports
        }
      withClue(
        ":feature:home is the only feature that may depend on other features. " +
          "Violates CLAUDE.md Rule 1. Offending files:\n" +
          violations.joinToString("\n") { (file, imps) ->
            "${file.path} -> ${imps.joinToString { it.name }}"
          },
      ) { violations.shouldBeEmpty() }
    }
  }
})
