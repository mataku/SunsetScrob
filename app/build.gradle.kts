import com.android.build.api.dsl.ApplicationExtension

plugins {
  id("sunsetscrob.android.application")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.metro")
  id("com.google.firebase.crashlytics")
  id("com.google.gms.google-services")
  id("com.google.devtools.ksp")
  id("app.cash.licensee")
  id("androidx.baselineprofile")
}

configure<ApplicationExtension>() {
  buildFeatures {
    buildConfig = true
  }

  namespace = "com.mataku.scrobscrob"
}

dependencies {
  implementation(project(":core"))
  implementation(project(":data:repository"))
  implementation(project(":data:api"))

  implementation(project(":feature:album"))
  implementation(project(":feature:artist"))
  implementation(project(":feature:auth"))
  implementation(project(":feature:scrobble"))
  implementation(project(":feature:account"))
  implementation(project(":feature:discover"))
  implementation(project(":feature:home"))

  implementation(libs.activity.ktx)
  implementation(libs.material)
  implementation(libs.coroutines)
  implementation(libs.kotlinx.collection)

  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)

  implementation(project(":ui_common"))
  implementation(libs.activity.compose)
  implementation(libs.compose.animation)
  implementation(libs.lifecycle.runtime.compose)


  implementation(libs.metrox.android)
  implementation(libs.core.splashscreen)
  implementation(libs.timber)

  implementation(libs.compose.material.icons.extended)
  implementation(libs.androidx.profileinstaller)
  baselineProfile(project(":benchmark"))
  debugImplementation(libs.showkase)
  implementation(libs.showkase.annotation)
  kspDebug(libs.showkase.processor)

  implementation(libs.coil.compose)
  implementation(libs.coil.okhttp)

  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.test.uiautomator)
  androidTestUtil(libs.androidx.test.services)
  androidTestImplementation(libs.compose.ui.test.junit4)
  androidTestImplementation(libs.compose.ui.test.android)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.ktor.client.mock)
  androidTestImplementation(libs.coroutines.test)
  debugImplementation(libs.compose.ui.test.manifest)
}

ksp {
  arg("skipPrivatePreviews", "true")
}

licensee {
  allow("Apache-2.0")
  allow("BSD-3-Clause")
  allowUrl("https://opensource.org/license/mit")
  allowUrl("https://developer.android.com/guide/playcore/license")
  allowUrl("https://developer.android.com/studio/terms.html")
}

tasks.withType(Test::class.java) {
  failOnNoDiscoveredTests = false
}
