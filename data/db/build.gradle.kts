plugins {
  id("sunsetscrob.android.feature")
  id("dagger.hilt.android.plugin")
  id("sunsetscrob.android.dagger")
  id("com.google.devtools.ksp")
  id("kotlinx-serialization")
  id(libs.plugins.sqldelight.get().pluginId)
}

android {
  namespace = "com.mataku.scrobscrob.data.db"
}

dependencies {
  implementation(libs.datastore.preferences)
  implementation(libs.serialization.json)
  implementation(libs.sqldelight.driver)
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.mataku.scrobscrob")
    }
  }
}
