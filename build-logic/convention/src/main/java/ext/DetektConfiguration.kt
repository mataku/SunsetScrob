package ext

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

fun Project.detektConfiguration() {
  val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

  extensions.configure<DetektExtension> {
    source.setFrom(files("src"))
    config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    basePath.set(rootProject.layout.projectDirectory)
    buildUponDefaultConfig.set(true)
    disableDefaultRuleSets.set(true)
    parallel.set(true)
    ignoreFailures.set(false)
  }

  dependencies.add("detektPlugins", libs.findLibrary("detekt-compose-rules").get())
  dependencies.add("detektPlugins", libs.findLibrary("detekt-ktlint-wrapper").get())

  tasks.withType<Detekt>().configureEach {
    reports {
      checkstyle.required.set(true)
      html.required.set(false)
      sarif.required.set(false)
      markdown.required.set(false)
    }
  }
}
