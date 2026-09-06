import ext.detektConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project

class LintConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply("dev.detekt")
      detektConfiguration()
    }
  }
}
