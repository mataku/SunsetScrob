import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class DaggerConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("dagger.hilt.android.plugin")
        apply("com.google.dagger.hilt.android")
        apply("com.google.devtools.ksp")
      }

      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
      dependencies {
        val hilt = libs.findLibrary("hilt-android").get()
        add("implementation", hilt)
        val hiltCompiler = libs.findLibrary("hilt-compiler").get()
        add("ksp", hiltCompiler)
      }
    }
  }
}
