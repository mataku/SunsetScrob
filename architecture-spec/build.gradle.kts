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
}

kotlin {
  jvmToolchain(17)
}
