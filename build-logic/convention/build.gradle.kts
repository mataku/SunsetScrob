plugins {
  `kotlin-dsl`
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
  implementation(libs.android.gradle.plugin)
  implementation(libs.kotlin.gradle.plugin)
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
  }
}
