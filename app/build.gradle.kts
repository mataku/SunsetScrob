plugins {
  id("sunsetscrob.android.application")
  id("sunsetscrob.android.compose")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
  id("com.google.firebase.crashlytics")
  id("com.google.gms.google-services")
}

apply(from = "${project.rootDir}/gradle/test_dependencies.gradle")

android {
  namespace = "com.mataku.scrobscrob"
    buildTypes {
        create("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
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
  kapt(libs.room.compiler)

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
  kapt(libs.hilt.android.compiler)

  implementation(project(":ui_common"))
  implementation(libs.activity.compose)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.material)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)
  implementation(libs.coil.compose)

  implementation(libs.accompanist.systemuicontroller)
  implementation(libs.hilt.navigation.compose)
  implementation(libs.core.splashscreen)
  implementation(libs.timber)

  implementation(libs.compose.material.icons.extended)
}

kapt {
  correctErrorTypes = true
  javacOptions {
    option("-Xmaxerrs", 1000)
  }
}

hilt {
  enableAggregatingTask = true
}
