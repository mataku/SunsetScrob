package ext

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

fun Project.kotlinWarningsConfiguration() {
  tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions.allWarningsAsErrors.set(true)
  }
}
