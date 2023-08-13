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
          it.maxParallelForks = 2
          it.useJUnitPlatform()
        }
      }
    }
    defaultConfig {
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
  }

  val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
  dependencies {
    val androidxTestCore = libs.findLibrary("androidx-test-core").get()
    val kotestRunner = libs.findLibrary("kotest-runner-junit5").get()
    val kotestAssertions = libs.findLibrary("kotest-assertions").get()
    val mockk = libs.findLibrary("mockk").get()
    val mockkAgentJvm = libs.findLibrary("mockk-agent-jvm").get()
    val turbine = libs.findLibrary("turbine").get()
    val testHelper = project(":test_helper")
    listOf(
      androidxTestCore,
      kotestRunner,
      kotestAssertions,
      mockk,
      mockkAgentJvm,
      turbine
    ).forEach {
      add("testImplementation", it)
    }
    add("testImplementation", testHelper)
  }
}
