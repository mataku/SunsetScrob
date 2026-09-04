import ext.androidLibraryTarget
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import ext.androidNamespace
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class KmpComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("org.jetbrains.compose")
        apply("org.jetbrains.kotlin.plugin.compose")
      }
      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
      val compose = extensions.getByType<ComposeExtension>().dependencies

      kotlinMultiplatform().apply {
        androidLibraryTarget {
          experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        }
        compilerOptions {
          freeCompilerArgs.add("-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi")
        }
        sourceSets.named("commonMain") {
          dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.animation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.findLibrary("jetbrains-compose-material3").get())
            implementation(libs.findLibrary("jetbrains-compose-ui-tooling-preview").get())
          }
        }
        sourceSets.named("androidMain") {
          dependencies {
            implementation(libs.findLibrary("jetbrains-compose-ui-tooling").get())
          }
        }
      }

      afterEvaluate {
        val namespace = kotlinMultiplatform().androidNamespace()
          ?: error("Set kotlin.android.namespace before applying sunsetscrob.compose")
        (extensions.getByType<ComposeExtension>() as ExtensionAware)
          .extensions.configure<ResourcesExtension>("resources") {
            packageOfResClass = "$namespace.generated.resources"
            publicResClass = true
          }
      }

      extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
        val composeReportEnabled =
          rootProject.providers.gradleProperty("composeCompilerReports").orNull == "true"
        if (composeReportEnabled) {
          reportsDestination.set(layout.buildDirectory.dir("compose_reports"))
          metricsDestination.set(layout.buildDirectory.dir("compose_metrics"))
        }
      }
    }
  }
}
