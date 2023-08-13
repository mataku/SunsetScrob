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
        apply("org.jetbrains.kotlin.kapt")

        extensions.configure<LibraryExtension> {
          androidSdkConfiguration()
          kotlinConfiguration()
          androidLintConfiguration()
          testConfiguration()
          buildTypes {
            getByName("debug") {
            }
            release {
            }
          }
        }
      }
    }
  }
}
