import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.getByType

class KmpScreenshotTestConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply("io.github.takahirom.roborazzi")
      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
      kotlinMultiplatform().apply {
        compilerOptions {
          freeCompilerArgs.add("-opt-in=androidx.compose.ui.test.ExperimentalTestApi")
        }
        sourceSets.named("jvmTest") {
          dependencies {
            implementation(project(":test_helper:integration"))
            implementation(libs.findLibrary("jetbrains-compose-ui-test").get())
            implementation(libs.findLibrary("junit-jupiter").get())
          }
        }
      }
      tasks.withType(Test::class.java).configureEach {
        failOnNoDiscoveredTests.set(hasProperty("onlyScreenshotTest"))
      }
    }
  }
}
