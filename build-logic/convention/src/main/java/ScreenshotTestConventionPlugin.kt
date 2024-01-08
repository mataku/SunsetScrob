import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ScreenshotTestConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("io.github.takahirom.roborazzi")
      }
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
              "win32-x86-64/attach_hotspot_windows.dll",
              "META-INF/AL2.0",
            )
            resources.excludes.addAll(excludePatterns)
          }
        }
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
          listOf(
            "androidx-test-ext-junit",
            "compose-ui-test-junit4",
            "robolectric",
            "roborazzi",
          ).forEach {
            add("testImplementation", libs.findLibrary(it).get())
          }
          add("testImplementation", project(":test_helper:integration"))
          add("debugImplementation", libs.findLibrary("compose-ui-test-manifest").get())
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
        defaultConfig {
          testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
      }
    }
  }
}
