package ext

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

fun Project.testConfiguration() {
  extensions.findByType(ApplicationExtension::class.java)?.apply {
    testOptions.unitTests.isIncludeAndroidResources = true
    testOptions.unitTests.all {
      it.maxParallelForks = Runtime.getRuntime().availableProcessors()
      it.useJUnitPlatform()
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
    listOf(
      androidxTestCore,
      kotestRunner,
      kotestAssertions,
      mockk,
      turbine,
      coroutinesTest,
    ).forEach {
      add("testImplementation", it)
    }
    add("testDebugImplementation", project(":test_helper:unit"))
  }
}
