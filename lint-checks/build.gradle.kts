plugins {
  alias(libs.plugins.kotlin)
}

dependencies {
  compileOnly(libs.lint.api)
  compileOnly(libs.lint.checks.dep)

  testImplementation(libs.lint)
  testImplementation(libs.lint.tests)
  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotest.assertions)
}

tasks.test {
  useJUnitPlatform()
}

tasks.jar {
  manifest {
    attributes(
      "Lint-Registry-v2" to "com.mataku.scrobscrob.lint.SunsetIssueRegistry",
    )
  }
}

kotlin {
  jvmToolchain(17)
}
