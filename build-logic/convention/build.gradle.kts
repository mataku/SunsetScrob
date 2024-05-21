plugins {
  `kotlin-dsl`
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  implementation(libs.android.gradle.plugin)
  implementation(libs.kotlin.gradle.plugin)
  implementation(libs.compose.compiler.plugin)
}

gradlePlugin {
  plugins {
    register("androidApplication") {
      id = "sunsetscrob.android.application"
      implementationClass = "ApplicationConventionPlugin"
    }
    register("androidFeature") {
      id = "sunsetscrob.android.feature"
      implementationClass = "FeatureConventionPlugin"
    }
    register("androidCompose") {
      id = "sunsetscrob.android.compose"
      implementationClass = "ComposeConventionPlugin"
    }
    register("androidDagger") {
      id = "sunsetscrob.android.dagger"
      implementationClass = "DaggerConventionPlugin"
    }
    register("androidScreenshotTest") {
      id = "sunsetscrob.android.test.screenshot"
      implementationClass = "ScreenshotTestConventionPlugin"
    }
  }
}
