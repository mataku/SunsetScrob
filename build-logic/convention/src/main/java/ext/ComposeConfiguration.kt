package ext

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import java.io.File

fun Project.composeConfiguration() {
  with(pluginManager) {
    apply("org.jetbrains.kotlin.plugin.compose")
  }
  val type = if (this.name == "app") {
    BaseAppModuleExtension::class.java
  } else {
    LibraryExtension::class.java
  }
  extensions.configure(type) {
    buildFeatures {
      compose = true
    }

    with(extensions.getByType<KotlinAndroidProjectExtension>()) {
      compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
      }
    }

    with(extensions.getByType<ComposeCompilerGradlePluginExtension>()) {
      includeSourceInformation.set(true)
      enableStrongSkippingMode.set(true)
    }
  }
}

private fun composeCompilerParameters(currentProject: Project): String {
  val compilerParameters = mutableListOf<String>()
  val composeMetricsEnabled =
    currentProject.rootProject.providers.gradleProperty("sunsetscrob.composeCompilerMetrics").orNull == "true"
  if (composeMetricsEnabled) {
    val metricsFolder = File(currentProject.layout.buildDirectory.asFile.get(), "compose_metrics")
    compilerParameters.add("-P")
    compilerParameters.add(
      "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath,
    )
  }

  val composeReportEnabled =
    currentProject.rootProject.providers.gradleProperty("sunsetscrob.composeCompilerReports").orNull == "true"
  if (composeReportEnabled) {
    val reportsFolder = File(currentProject.layout.buildDirectory.asFile.get(), "compose_reports")
    compilerParameters.add("-P")
    compilerParameters.add(
      "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath,
    )
  }
  return compilerParameters.toList().joinToString(separator = " ")
}
