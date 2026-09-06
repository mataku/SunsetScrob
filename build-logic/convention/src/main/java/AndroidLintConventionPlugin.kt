import com.android.build.api.dsl.ApplicationExtension
import ext.androidLintConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLintConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.withPlugin("com.android.application") {
        extensions.configure<ApplicationExtension> {
          androidLintConfiguration()
        }
      }
    }
  }
}
