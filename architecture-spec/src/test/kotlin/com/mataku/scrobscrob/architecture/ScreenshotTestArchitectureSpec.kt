package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import java.io.File

class ScreenshotTestArchitectureSpec : DescribeSpec({

  val testFiles = Konsist.scopeFromTest().files
    .filterNot { it.path.contains("/architecture-spec/") }

  describe("Screenshot test conventions (testing.md)") {

    it("JVM screenshot classes carry @Tag(\"VRT\")") {
      val violations = testFiles
        .filter { it.path.contains("/src/jvmTest/") }
        .filter { it.text.contains("captureScreenshot(") }
        .flatMap { it.classes() }
        .filterNot { cls -> cls.annotations.any { it.name == "Tag" && it.text.contains("\"VRT\"") } }
      withClue(
        "Classes that call captureScreenshot under src/jvmTest must be annotated @Tag(\"VRT\") so " +
          "-PonlyScreenshotTest / -PexcludeScreenshotTest can filter them. Offending classes:\n" +
          violations.joinToString("\n") { it.fullyQualifiedName ?: it.name },
      ) { violations.shouldBeEmpty() }
    }

    it("modules applying sunsetscrob.library have no src/main or src/test") {
      val root = File(System.getProperty("user.dir")).parentFile
      val violations = root.walkTopDown()
        .onEnter { it.name != ".git" && it.name != "build" }
        .filter { it.name == "build.gradle.kts" }
        .filter { it.readText().contains("id(\"sunsetscrob.library\")") }
        .map { it.parentFile }
        .filter { File(it, "src/main").exists() || File(it, "src/test").exists() }
        .map { it.relativeTo(root).path }
        .toList()
      withClue(
        "KMP modules keep sources in commonMain/androidMain/jvmMain and tests in jvmTest. Offending modules:\n" +
          violations.joinToString("\n"),
      ) { violations.shouldBeEmpty() }
    }
  }
})
