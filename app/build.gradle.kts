plugins {
  id("sunsetscrob.android.application")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.dagger")
  id("sunsetscrob.android.test.integration")
  id("com.google.firebase.crashlytics")
  id("com.google.gms.google-services")
  id("com.google.devtools.ksp")
}

android {
  buildFeatures {
    buildConfig = true
  }

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
  implementation(project(":feature:chart"))

  implementation(libs.activity.ktx)
  implementation(libs.material)
  implementation(libs.coroutines)

  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)

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
  debugImplementation(libs.showkase)
  implementation(libs.showkase.annotation)
  kspDebug(libs.showkase.processor)
}

ksp {
  arg("skipPrivatePreviews", "true")
}

hilt {
  enableAggregatingTask = true
}
