package ext

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

fun Project.testConfiguration() {
  val type = if (this.name == "app") {
    BaseAppModuleExtension::class.java
  } else {
    LibraryExtension::class.java
  }
  extensions.configure(type) {
    testOptions {
      unitTests {
        isIncludeAndroidResources = true
        all {
          it.maxParallelForks = Runtime.getRuntime().availableProcessors()
          it.useJUnitPlatform {
            // for junit 4 tests
            includeEngines("junit-vintage")
          }
        }
      }
    }
  }

  val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
  dependencies {
    val androidxTestCore = libs.findLibrary("androidx-test-core").get()
    val kotestRunner = libs.findLibrary("kotest-runner-junit5").get()
    val kotestAssertions = libs.findLibrary("kotest-assertions").get()
    val mockk = libs.findLibrary("mockk").get()
    val turbine = libs.findLibrary("turbine").get()
    listOf(
      androidxTestCore,
      kotestRunner,
      kotestAssertions,
      mockk,
      turbine
    ).forEach {
      add("testImplementation", it)
    }
    add("testDebugImplementation", project(":test_helper:unit"))
  }
}
