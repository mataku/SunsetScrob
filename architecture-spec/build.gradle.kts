plugins {
  alias(libs.plugins.kotlin)
  id("sunsetscrob.lint")
}

dependencies {
  testImplementation(libs.konsist)
  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotest.assertions)
}

tasks.test {
  useJUnitPlatform()
  // Konsist reads every module's sources at runtime. Without declaring them, Gradle treats this
  // task as UP-TO-DATE / FROM-CACHE whenever only those sources change, and the specs never run.
  inputs.files(
    fileTree(rootDir) {
      include("**/src/**/*.kt", "**/*.gradle.kts")
      exclude("**/build/**", ".gradle/**", "build-logic/convention/build/**")
    },
  ).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("konsistSources")
}

kotlin {
  jvmToolchain(17)
}
