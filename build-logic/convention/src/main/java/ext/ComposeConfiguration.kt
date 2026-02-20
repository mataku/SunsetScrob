package ext

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

fun Project.composeConfiguration() {
  with(pluginManager) {
    apply("org.jetbrains.kotlin.plugin.compose")
  }
  extensions.configure<CommonExtension> {
    buildFeatures.compose = true
  }
  extensions.getByType<KotlinAndroidProjectExtension>().compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
    if (name != "core") {
      freeCompilerArgs.add("-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi")
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
