plugins {
  id("sunsetscrob.android.feature")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
  id("sunsetscrob.android.unittest")
}

android {
  namespace = "com.mataku.scrobscrob.data.db"
}

dependencies {
  implementation(project(":core"))

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)

  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  kapt(libs.room.compiler)

  implementation(libs.datastore.preferences)
}
