plugins {
  id("sunsetscrob.android.application")
  id("sunsetscrob.android.compose")
  id("com.google.firebase.crashlytics")
  id("com.google.gms.google-services")
  id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

apply(from = "${project.rootDir}/gradle/test_dependencies.gradle")

android {
  namespace = "com.mataku.scrobscrob"
}

dependencies {
  implementation(project(":core"))
  implementation(project(":data:repository"))

  implementation(project(":feature:album"))
  implementation(project(":feature:artist"))
  implementation(project(":feature:auth"))
  implementation(project(":feature:scrobble"))
  implementation(project(":feature:account"))

  implementation(libs.activity.ktx)
  implementation(libs.material)
  implementation(libs.coroutines)

  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)

  implementation(libs.room.runtime)
  ksp(libs.room.compiler)

  implementation(project(":ui_common"))
  implementation(libs.activity.compose)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.material)
  implementation(libs.compose.navigation)
  implementation(libs.coil.compose)
  implementation(libs.accompanist.navigation.animation)
  implementation(libs.accompanist.systemuicontroller)
  implementation(libs.hilt.navigation.compose)
  implementation(libs.core.splashscreen)
  implementation(libs.timber)

  implementation(libs.koin.android)

  testImplementation(libs.koin.test)
}
