import com.android.build.gradle.LibraryExtension
import ext.androidLintConfiguration
import ext.androidSdkConfiguration
import ext.kotlinConfiguration
import ext.testConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class FeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")

        extensions.configure<LibraryExtension> {
          androidSdkConfiguration()
          kotlinConfiguration()
          androidLintConfiguration()
          packaging {
            val excludePatterns = listOf(
              "META-INF/LICENSE.md",
              "META-INF/LICENSE-notice.md"
            )
            resources.excludes.addAll(excludePatterns)
          }
          buildTypes {
            getByName("debug") {
            }
            release {
            }
          }
        }
      }
      testConfiguration()
    }
  }
}
