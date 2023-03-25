plugins {
  id("sunsetscrob.android.feature")
  id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

apply(from = "${project.rootDir}/gradle/test_dependencies.gradle")

android {
  namespace = "com.mataku.scrobscrob.data.db"
}

dependencies {
  implementation(project(":core"))

  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  ksp(libs.room.compiler)

  implementation(libs.datastore.preferences)
  implementation(libs.koin.android)

  testImplementation(libs.koin.test)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    jvmTarget = "11"
  }
}
