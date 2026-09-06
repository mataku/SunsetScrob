package com.mataku.scrobscrob.architecture

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.io.File

class BuildScriptArchitectureSpec : DescribeSpec({

  val projectRoot = generateSequence(File(".").absoluteFile) { it.parentFile }
    .first { File(it, "settings.gradle.kts").exists() }

  describe("Root build script conventions") {

    it("the root build.gradle.kts holds no configuration blocks") {
      val rootBuildScript = File(projectRoot, "build.gradle.kts").readText()
      val forbidden = listOf("subprojects", "allprojects")
      val found = forbidden.filter { rootBuildScript.contains(Regex("(^|\\s)$it\\s*\\{")) }

      withClue(
        "The root build.gradle.kts is declaration-only. Gradle configuration belongs in a convention " +
          "plugin under build-logic/ — see `.claude/rules/architecture.md` \"Convention Plugins\" and " +
          "ext/AndroidLintConfiguration.kt / ext/DetektConfiguration.kt for the pattern. Found: $found",
      ) { found shouldBe emptyList() }
    }
  }
})
