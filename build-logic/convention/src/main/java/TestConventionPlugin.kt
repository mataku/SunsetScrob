import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class TestConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      val type = if (target.name == "app") {
        BaseAppModuleExtension::class
      } else {
        LibraryExtension::class
      }
      extensions.configure(type) {
        testOptions {
          unitTests {
            all {
              it.useJUnitPlatform()
              isIncludeAndroidResources = true
              it.maxParallelForks = Runtime.getRuntime().availableProcessors()
            }
          }
        }
      }
      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

      dependencies {
        listOf(
          "androidx-test-core",
          "kotest-runner-junit5",
          "kotest-assertions",
          "mockk",
          "mockk-agent-jvm"
        ).forEach {
          add("testImplementation", libs.findLibrary(it).get())
        }
        add("testImplementation", project(":test_helper"))
      }
    }
  }
}
