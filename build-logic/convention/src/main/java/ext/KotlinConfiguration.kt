package ext

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.File

fun CommonExtension<*, *, *, *>.kotlinConfiguration(
  rootProject: Project
) {
  compileSdk = 33
  defaultConfig {
    minSdk = 29
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
    jvmTarget = "11"
    freeCompilerArgs =
      freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
  }
}

private fun composeCompilerParameters(rootProject: Project): List<String> {
  val compilerParameters = mutableListOf<String>()
  val composeMetricsEnabled =
    rootProject.providers.gradleProperty("sunsetscrob.composeCompilerMetrics").orNull == "true"
  if (composeMetricsEnabled) {
    val metricsFolder = File(rootProject.buildDir, "compose_metrics")
    compilerParameters.add("-P")
    compilerParameters.add(
      "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath,
    )
  }

  val composeReportEnabled =
    rootProject.providers.gradleProperty("sunsetscrob.composeCompilerReports").orNull == "true"
  if (composeReportEnabled) {
    val reportsFolder = File(rootProject.buildDir, "compose_metrics")
    compilerParameters.add("-P")
    compilerParameters.add(
      "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath,
    )
  }
  return compilerParameters.toList()
}

