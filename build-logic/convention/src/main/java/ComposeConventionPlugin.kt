import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
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
      val type = if (target.name == "app") {
        BaseAppModuleExtension::class.java
      } else {
        LibraryExtension::class.java
      }
      extensions.configure(type) {
        composeConfiguration(libs, target)
      }

      dependencies {
        val composeBom = libs.findLibrary("compose-bom").get()
        add("implementation", platform(composeBom))
      }
    }
  }
}
