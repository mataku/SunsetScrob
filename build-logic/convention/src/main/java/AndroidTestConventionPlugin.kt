import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidTestConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      val type = if (target.name == "app") {
        BaseAppModuleExtension::class
      } else {
        LibraryExtension::class
      }
      extensions.configure(type) {
        if (type == LibraryExtension::class) {
          packaging {
            val excludePatterns = listOf(
              "META-INF/LICENSE.md",
              "META-INF/LICENSE-notice.md",
              "win32-x86-64/attach_hotspot_windows.dll"
            )
            resources.excludes.addAll(excludePatterns)
          }
        }
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
          listOf(
            "compose-ui-test-junit4",
            "compose-ui-test-manifest",
            "mockk-android",
            "androidx-test-core"
          ).forEach {
            add("androidTestImplementation", libs.findLibrary(it).get())
          }
          add("androidTestImplementation", project(":test_helper:integration"))
        }

        testOptions {
          managedDevices {
            devices.maybeCreate("pixel4Api31", ManagedVirtualDevice::class.java).apply {
              device = "Pixel 4"
              apiLevel = 31
              systemImageSource = "aosp"
            }
          }
        }
      }
    }
  }
}
