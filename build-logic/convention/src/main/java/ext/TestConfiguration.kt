package ext

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

private const val VRT_TAG = "com.mataku.scrobscrob.test_helper.integration.VRT"

fun Project.testConfiguration() {
  val project = this
  extensions.findByType(ApplicationExtension::class.java)?.apply {
    testOptions.unitTests.isIncludeAndroidResources = true
    testOptions.unitTests.all {
      it.maxParallelForks = Runtime.getRuntime().availableProcessors()
      it.useJUnitPlatform {
        if (project.hasProperty("excludeScreenshotTest")) {
          excludeTags(VRT_TAG)
        }
        if (project.hasProperty("onlyScreenshotTest")) {
          includeTags(VRT_TAG)
          excludeEngines("kotest")
        }
      }
      it.jvmArgs("-Xshare:off")
    }
  }
  extensions.findByType(LibraryExtension::class.java)?.apply {
    testOptions.unitTests.isIncludeAndroidResources = true
    testOptions.unitTests.all {
      it.maxParallelForks = Runtime.getRuntime().availableProcessors()
      it.useJUnitPlatform {
        if (project.hasProperty("excludeScreenshotTest")) {
          excludeTags(VRT_TAG)
        }
        if (project.hasProperty("onlyScreenshotTest")) {
          includeTags(VRT_TAG)
          excludeEngines("kotest")
        }
      }
      it.jvmArgs("-Xshare:off")
    }
  }

  val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
  dependencies {
    val androidxTestCore = libs.findLibrary("androidx-test-core").get()
    val kotestRunner = libs.findLibrary("kotest-runner-junit5").get()
    val kotestAssertions = libs.findLibrary("kotest-assertions").get()
    val mockk = libs.findLibrary("mockk").get()
    val turbine = libs.findLibrary("turbine").get()
    val coroutinesTest = libs.findLibrary("coroutines-test").get()
    val junitVintageEngine = libs.findLibrary("junit-vintage-engine").get()
    listOf(
      androidxTestCore,
      kotestRunner,
      kotestAssertions,
      mockk,
      turbine,
      coroutinesTest,
      junitVintageEngine,
    ).forEach {
      add("testImplementation", it)
    }
    add("testDebugImplementation", project(":test_helper:unit"))
  }
}
