plugins {
  `kotlin-dsl`
}

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
  jvmToolchain(21)
}

dependencies {
  implementation(libs.android.gradle.plugin)
  implementation(libs.kotlin.gradle.plugin)
  implementation(libs.compose.compiler.plugin)
  implementation(libs.metro.gradle.plugin)
  implementation(libs.jetbrains.compose.gradle.plugin)
}

gradlePlugin {
  plugins {
    register("androidApplication") {
      id = "sunsetscrob.android.application"
      implementationClass = "ApplicationConventionPlugin"
    }
    register("androidCompose") {
      id = "sunsetscrob.android.compose"
      implementationClass = "ComposeConventionPlugin"
    }
    register("androidMetro") {
      id = "sunsetscrob.android.metro"
      implementationClass = "MetroConventionPlugin"
    }
    register("kmpLibrary") {
      id = "sunsetscrob.library"
      implementationClass = "KmpLibraryConventionPlugin"
    }
    register("kmpCompose") {
      id = "sunsetscrob.compose"
      implementationClass = "KmpComposeConventionPlugin"
    }
    register("kmpMetro") {
      id = "sunsetscrob.metro"
      implementationClass = "KmpMetroConventionPlugin"
    }
    register("kmpScreenshotTest") {
      id = "sunsetscrob.test.screenshot"
      implementationClass = "KmpScreenshotTestConventionPlugin"
    }
  }
}
