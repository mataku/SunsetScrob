enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
  includeBuild("build-logic")
  repositories {
    google {
      content {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google {
      content {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    // Not exclusiveContent on google, and not a blanket com.google exclude: Maven Central is the only host for guava, gson, protobuf and tink.
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
}

include(":app")
include(":core")
include(":ui_common")
include(":data:api")
include(":data:repository")
include(":data:db")
include(":feature:album")
include(":feature:artist")
include(":feature:auth")
include(":feature:scrobble")
include(":feature:account")
include(":test_helper")
include(":test_helper:unit")
include(":test_helper:integration")
include(":feature:discover")
include(":benchmark")
include(":feature:home")
include(":architecture-spec")
