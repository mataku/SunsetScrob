import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

class KmpMetroConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply("dev.zacsweers.metro")
      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
      kotlinMultiplatform().sourceSets.named("commonMain") {
        dependencies {
          implementation(libs.findLibrary("metrox-viewmodel-compose").get())
        }
      }
    }
  }
}
