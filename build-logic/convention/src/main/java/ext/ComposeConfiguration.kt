package ext

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.File

fun CommonExtension<*, *, *, *, *, *>.composeConfiguration(
  libs: VersionCatalog,
  currentProject: Project
) {
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion =
      libs.findVersion("compose.compiler.extension").get().toString()
  }

  (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
    freeCompilerArgs = freeCompilerArgs + composeCompilerParameters(
      currentProject
    )
  }
}

private fun composeCompilerParameters(currentProject: Project): List<String> {
  val compilerParameters = mutableListOf<String>()
  val composeMetricsEnabled =
    currentProject.rootProject.providers.gradleProperty("sunsetscrob.composeCompilerMetrics").orNull == "true"
  if (composeMetricsEnabled) {
    val metricsFolder = File(currentProject.buildDir, "compose_metrics")
    compilerParameters.add("-P")
    compilerParameters.add(
      "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath,
    )
  }

  val composeReportEnabled =
    currentProject.rootProject.providers.gradleProperty("sunsetscrob.composeCompilerReports").orNull == "true"
  if (composeReportEnabled) {
    val reportsFolder = File(currentProject.buildDir, "compose_reports")
    compilerParameters.add("-P")
    compilerParameters.add(
      "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath,
    )
  }
  return compilerParameters.toList()
}
