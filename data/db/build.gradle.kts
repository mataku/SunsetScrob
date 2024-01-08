plugins {
  id("sunsetscrob.android.feature")
  id("dagger.hilt.android.plugin")
  id("sunsetscrob.android.dagger")
  id("com.google.devtools.ksp")
  id("kotlinx-serialization")
}

android {
  namespace = "com.mataku.scrobscrob.data.db"
}

dependencies {
  implementation(libs.datastore.preferences)
  implementation(libs.serialization.json)
}
