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
        sourceSets.named("jvmTest") {
          languageSettings.optIn("androidx.compose.ui.test.ExperimentalTestApi")
          dependencies {
            implementation(project(":test_helper:integration"))
            implementation(libs.findLibrary("jetbrains-compose-ui-test").get())
            implementation(libs.findLibrary("junit-jupiter").get())
          }
        }
      }
      tasks.withType(Test::class.java).configureEach {
        failOnNoDiscoveredTests.set(hasProperty("onlyScreenshotTest"))
        systemProperty("user.language", "en")
        systemProperty("user.country", "US")
        // Roborazzi renders through Compose Desktop, so each forked test JVM
        // registers as a GUI app and appears in the macOS Dock. Headless mode
        // would break the Skia render path, so keep AWT functional and only
        // suppress the Dock and menu bar entry. Ignored outside macOS.
        systemProperty("apple.awt.UIElement", "true")
      }
    }
  }
}
