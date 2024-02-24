enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
  includeBuild("build-logic")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
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
include(":feature:chart")
include(":benchmark")
