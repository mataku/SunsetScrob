plugins {
  id("sunsetscrob.android.feature")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
}

apply(from = "${project.rootDir}/gradle/test_dependencies.gradle")

dependencies {
  implementation(project(":core"))

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)

  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  kapt(libs.room.compiler)

  implementation(libs.datastore.preferences)
}
