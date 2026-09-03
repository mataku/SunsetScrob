import ext.kmpTargetsConfiguration
import ext.kmpTestConfiguration
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

class KmpLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("org.jetbrains.kotlin.multiplatform")
        apply("com.android.kotlin.multiplatform.library")
        apply("org.jetbrains.kotlin.plugin.serialization")
      }
      kmpTargetsConfiguration()
      kmpTestConfiguration()

      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
      kotlinMultiplatform().sourceSets.named("jvmTest") {
        dependencies {
          listOf(
            "kotest-runner-junit5",
            "kotest-assertions",
            "mockk",
            "turbine",
            "coroutines-test",
          ).forEach { implementation(libs.findLibrary(it).get()) }
          if (target.path != ":test_helper:unit") {
            implementation(project(":test_helper:unit"))
          }
        }
      }
    }
  }
}
