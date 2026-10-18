package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty

// HomeScreen creates its tabs' ViewModels through metroViewModel(), which needs the app graph, so it
// cannot render in a JVM screenshot test. HomeTabsTest and the ScrobbleScreen / TopAlbumsScreen /
// TopArtistsScreen tests cover what it composes.
private val screensWithoutScreenshotTest = setOf("HomeScreen")

class TestPresenceArchitectureSpec : DescribeSpec({

  val production = Konsist.scopeFromProduction()
  val testFileNames = Konsist.scopeFromTest().files
    .filterNot { it.path.contains("/architecture-spec/") }
    .map { it.name }
    .toSet()

  describe("Test presence (testing.md)") {

    it("every `*ViewModel` has a `*ViewModelSpec`") {
      val missing = production.classes()
        .filter { it.name.endsWith("ViewModel") }
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        .map { it.name }
        .filterNot { "${it}Spec" in testFileNames }

      withClue(
        "Each ViewModel needs a Kotest spec named `<ViewModel>Spec.kt` (testing.md). Missing specs for:\n" +
          missing.joinToString("\n"),
      ) { missing.shouldBeEmpty() }
    }

    it("every `*Endpoint` in :data:api has a `*EndpointSpec`") {
      val missing = production.classes()
        .filter { it.path.contains("/data/api/") }
        .filter { it.name.endsWith("Endpoint") }
        .map { it.name }
        .filterNot { "${it}Spec" in testFileNames }

      withClue(
        "Each Endpoint needs a `<Endpoint>Spec.kt` that checks its URL, method and JSON decoding " +
          "through MockEngine (testing.md). Missing specs for:\n" + missing.joinToString("\n"),
      ) { missing.shouldBeEmpty() }
    }

    it("every screen in a feature module or :ui_common has a `*ScreenTest`") {
      val missing = production.files
        .filter { it.path.contains("/feature/") || it.path.contains("/ui_common/") }
        .filter { it.path.contains("/screen/") }
        .filter { it.name.endsWith("Screen") }
        .map { it.name }
        .filterNot { it in screensWithoutScreenshotTest }
        .filterNot { "${it}Test" in testFileNames }

      withClue(
        "Each `*Screen.kt` under a `screen` package needs a Roborazzi test named `<Screen>Test.kt` " +
          "(testing.md, create-vrt skill). Missing screenshot tests for:\n" + missing.joinToString("\n"),
      ) { missing.shouldBeEmpty() }
    }
  }
})
