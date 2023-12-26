plugins {
  id("sunsetscrob.android.feature")
  id("dagger.hilt.android.plugin")
  id("sunsetscrob.android.dagger")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.mataku.scrobscrob.data.db"
}

dependencies {
  implementation(project(":core"))
  implementation(libs.datastore.preferences)
}
