import ext.composeConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
      composeConfiguration()

      dependencies {
        val composeBom = libs.findLibrary("compose-bom").get()
        add("implementation", platform(composeBom))
        add("androidTestImplementation", platform(composeBom))
      }
    }
  }
}
