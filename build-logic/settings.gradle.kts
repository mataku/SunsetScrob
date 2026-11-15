dependencyResolutionManagement {
  repositories {
    // Renovate merges the repositories of every Gradle file, so these filters must match the root settings.gradle.kts.
    // An unfiltered entry here makes it look up every library in both repositories again.
    google {
      content {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral {
      content {
        excludeGroupAndSubgroups("androidx")
        excludeGroupAndSubgroups("com.android")
        excludeGroupAndSubgroups("com.google.android.gms")
        excludeGroupAndSubgroups("com.google.android.material")
        excludeGroupAndSubgroups("com.google.android.play")
        excludeGroupAndSubgroups("com.google.firebase")
        excludeGroupAndSubgroups("com.google.gms")
      }
    }
  }
  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}

include(":convention")
