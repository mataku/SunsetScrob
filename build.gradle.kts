buildscript {
  repositories {
    google()
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
  }
  dependencies {
    classpath(libs.android.gradle.plugin)
    classpath(libs.kotlin.gradle.plugin)
    classpath(libs.serialization.plugin)
    classpath(libs.crashlytics.gradle.plugin)
    classpath(libs.google.services.plugin)
    classpath(libs.hilt.gradle.plugin)
    classpath(libs.aboutlibraries.plugin)

    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}
