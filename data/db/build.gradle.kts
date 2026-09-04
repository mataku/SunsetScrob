plugins {
  id("sunsetscrob.library")
  id("sunsetscrob.metro")
  alias(libs.plugins.sqldelight)
}

kotlin {
  android {
    namespace = "com.mataku.scrobscrob.data.db"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.coroutines.core)
      implementation(libs.serialization.json)
      implementation(libs.datastore)
      implementation(libs.datastore.preferences)
      implementation(libs.sqldelight.runtime)
      implementation(libs.sqldelight.coroutines)
    }
    androidMain.dependencies {
      implementation(libs.coroutines)
      implementation(libs.datastore.tink)
      implementation(libs.sqldelight.driver)
    }
  }
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.mataku.scrobscrob")
    }
  }
}
