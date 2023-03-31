plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
  id("com.mikepenz.aboutlibraries.plugin")
}

apply(from = "${project.rootDir}/gradle/test_dependencies.gradle")

android {
  aboutLibraries {
    // - If the automatic registered android tasks are disabled, a similar thing can be achieved manually
    // - `./gradlew app:exportLibraryDefinitions -PexportPath=src/main/res/raw`
    // - the resulting file can for example be added as part of the SCM
    registerAndroidTasks = false
    // Define the output file name. Modifying this will disable the automatic meta data discovery for supported platforms.
    outputFileName = "aboutlibraries.json"
    // Allow to enable "offline mode", will disable any network check of the plugin (including [fetchRemoteLicense] or pulling spdx license texts)
    offlineMode = false
    // Enable fetching of "remote" licenses.  Uses the API of supported source hosts
    // See https://github.com/mikepenz/AboutLibraries#special-repository-support
    fetchRemoteLicense = true
    // Allows to exclude some fields from the generated meta data field.
    excludeFields = arrayOf("developers", "funding")
    // Define the strict mode, will fail if the project uses licenses not allowed
    // - This will only automatically fail for Android projects which have `registerAndroidTasks` enabled
    // For non Android projects, execute `exportLibraryDefinitions`
    strictMode = com.mikepenz.aboutlibraries.plugin.StrictMode.FAIL
    // Allowed set of licenses, this project will be able to use without build failure
    allowedLicenses = arrayOf("Apache-2.0", "asdkl", "mit", "mpl_2_0")
    // Enable the duplication mode, allows to merge, or link dependencies which relate
    duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    // Configure the duplication rule, to match "duplicates" with
    duplicationRule = com.mikepenz.aboutlibraries.plugin.DuplicateRule.SIMPLE
  }

  namespace = "com.mataku.scrobscrob.account"
}

dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))

  implementation(libs.activity.compose)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.material)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)
  implementation(libs.compose.material.icons.extended)

  implementation(libs.hilt.navigation.compose)

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
  kapt(libs.hilt.android.compiler)

  implementation(libs.coroutines)
  implementation(libs.timber)

  implementation(libs.accompanist.navigation.animation)
  implementation(libs.accompanist.systemuicontroller)

  implementation(libs.aboutlibraries)
  implementation(libs.aboutlibraries.compose)

  implementation(libs.app.update)
}
