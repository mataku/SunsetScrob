plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
}

apply(from = "${project.rootDir}/gradle/test_dependencies.gradle")

android {
  namespace = "com.mataku.scrobscrob.auth"
}

dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))
  implementation(libs.activity.compose)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.material)
  implementation(libs.compose.navigation)
  implementation(libs.coil.compose)
  implementation(libs.accompanist.navigation.animation)
  implementation(libs.compose.material.icons.extended)
  implementation(libs.accompanist.systemuicontroller)

  implementation(libs.coroutines)
  implementation(libs.timber)

  implementation(libs.koin.android)
  implementation(libs.koin.compose)

  testImplementation(project(":test_helper"))

  testImplementation(libs.koin.test)
}
