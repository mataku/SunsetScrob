package ext

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

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
      val composeReportEnabled =
        rootProject.providers.gradleProperty("composeCompilerReports").orNull == "true"

      if (composeReportEnabled) {
        reportsDestination.set(layout.buildDirectory.dir("compose_reports"))
        metricsDestination.set(layout.buildDirectory.dir("compose_metrics"))
      }
    }
  }
}
