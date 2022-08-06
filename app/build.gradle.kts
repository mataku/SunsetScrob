import dependency.Versions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
  id("com.google.firebase.crashlytics")
  id("com.google.gms.google-services")
}

apply(from = "lint-checks.gradle")
apply(from = "${project.rootDir}/gradle/test_dependencies.gradle")
apply(from = "${project.rootDir}/gradle/test_options.gradle")

android {
  compileSdk = Versions.compileSdkVersion
  buildFeatures {
    compose = true
  }

  defaultConfig {
    applicationId = "com.mataku.scrobscrob"
    minSdk = Versions.minSdkVersion
    targetSdk = Versions.targetSdkVersion
    versionCode = 98
    versionName = "0.9.8"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables.useSupportLibrary = true
    proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
  }

  signingConfigs {
    getByName("debug") {
      storeFile = file("../debug.keystore")
    }
    create("release") {
      storeFile = file("../SunsetScrob.jks")
      storePassword = System.getenv("SUNSET_STORE_PASSWORD")
      keyAlias = System.getenv("SUNSET_KEY_ALIAS")
      keyPassword = System.getenv("SUNSET_KEY_PASSWORD")
    }
  }

  buildTypes {
    getByName("debug") {
      isMinifyEnabled = false
      applicationIdSuffix = ".dev"
      signingConfig = signingConfigs.getByName("debug")
      isDebuggable = true
    }
    release {
      isMinifyEnabled = true
      signingConfig = signingConfigs.getByName("release")
      isDebuggable = false
    }
  }

  lint {
    abortOnError = false
    textReport = true
    xmlReport = false
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.kotlin.compiler.extension.get()
  }

  packagingOptions {
    val excludePatterns = listOf(
      "META-INF/atomicfu.kotlin_module",
      "META-INF/kotlinx-coroutines-io.kotlin_module",
      "META-INF/kotlinx-io.kotlin_module",
      "META-INF/ktor-client-json.kotlin_module",
      "META-INF/ktor-client-core.kotlin_module",
      "META-INF/ktor-http.kotlin_module",
      "META-INF/ktor-utils.kotlin_module",
      "META-INF/kotlinx-coroutines-core.kotlin_module",
      "META-INF/kotlinx-serialization-runtime.kotlin_module",
      "META-INF/gradle/incremental.annotation.processors"
    )
    resources.excludes.addAll(excludePatterns)
  }

//    dynamicFeatures [":feature:licenses"]
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
  implementation(libs.compose.navigation)
  implementation(libs.coil.compose)
  implementation(libs.accompanist.navigation.animation)
  implementation(libs.accompanist.systemuicontroller)
  implementation(libs.hilt.navigation.compose)
  implementation(libs.core.splashscreen)
}

kapt {
  correctErrorTypes = true
  javacOptions {
    option("-Xmaxerrs", 1000)
  }
}

tasks.withType(KotlinCompile::class).all {
  kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
  }
}
