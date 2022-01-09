package dependency

const val kotlinVersion = "1.5.31"

object Deps {
    private const val ktorVersion = "1.5.1"
    private const val hiltVersion = "2.40.4"
    const val kotlinCompilerExtensionVersion = "1.0.5"

    object Compose {
        // Integration with activities
        const val activity = "androidx.activity:activity-compose:1.3.1"

        // Compose Material Design
        const val material = "androidx.compose.material:material:1.0.5"

        // Animations
        const val animation = "androidx.compose.animation:animation:1.0.5"

        // Tooling support (Previews, etc.)
        const val uiTooling = "androidx.compose.ui:ui-tooling:1.0.5"

        // Integration with ViewModels
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"

        const val navigation = "androidx.navigation:navigation-compose:2.4.0-rc01"
    }

    object Accompanist {
        const val navigationAnimation =
            "com.google.accompanist:accompanist-navigation-animation:0.22.0-rc"
    }

    const val activityKtx = "androidx.activity:activity-ktx:1.4.0"

    const val appCompat = "androidx.appcompat:appcompat:1.3.1"
    const val browser = "androidx.browser:browser:1.0.0"
    const val cardView = "androidx.cardview:cardview:1.0.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:2.10.1"

    const val coil = "io.coil-kt:coil-compose:1.4.0"

    const val datastorePreferences = "androidx.datastore:datastore-preferences:1.0.0"

    const val epoxy = "com.airbnb.android:epoxy:3.11.0"
    const val epoxyProcessor = "com.airbnb.android:epoxy-processor:3.11.0"

    const val firebaseCore = "com.google.firebase:firebase-core:17.1.1"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics:17.3.1"

    const val glide = "com.github.bumptech.glide:glide:4.11.0"
    const val glideCompiler = "com.github.bumptech.glide:compiler:4.11.0"

    const val hilt = "com.google.dagger:hilt-android:$hiltVersion"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$hiltVersion"

    const val hiltLifecycleViewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0"
    const val hiltAndroidXCompiler = "androidx.hilt:hilt-compiler:1.0.0"

    const val koinAndroid = "org.koin:koin-android:2.0.1"
    const val koinAndroidXScope = "org.koin:koin-androidx-scope:2.0.1"
    const val koinAndroidXViewModel = "org.koin:koin-androidx-viewmodel:2.0.1"

    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3"
    const val kotlinSerializationRuntime =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0"

    const val ktorClientOkhttp = "io.ktor:ktor-client-okhttp:$ktorVersion"
    const val ktorClientJsonJvm = "io.ktor:ktor-client-serialization-jvm:$ktorVersion"
    const val ktorClientLogging = "io.ktor:ktor-client-logging:$ktorVersion"
    const val ktorClientCore = "io.ktor:ktor-client-core:$ktorVersion"

    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.0.0"
    const val lifecycleViewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0"
    const val materialComponent = "com.google.android.material:material:1.1.0"

    const val okhttp = "com.squareup.okhttp3:okhttp:4.9.1"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.1"

    const val preference = "androidx.preference:preference:1.0.0"

    const val roomRuntime = "androidx.room:room-runtime:2.4.0-rc01"
    const val roomCompiler = "androidx.room:room-compiler:2.4.0-rc01"

    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:7.0.3"
        const val androidJunit5 = "de.mannodermaus.gradle.plugins:android-junit5:1.3.1.1"
        const val licenseTools = "gradle.plugin.com.cookpad.android.plugin:plugin:1.2.8"
        const val firebaseCrashlyticsGradlePlugin =
            "com.google.firebase:firebase-crashlytics-gradle:2.2.0"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:0.27.0"
        const val googleServices = "com.google.gms:google-services:4.3.8"
        const val kotlinSerialization =
            "org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion"
    }
}

object UnitTestDeps {
    const val androidxTestCore = "androidx.test:core:1.1.0"
    const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
    const val robolectric = "org.robolectric:robolectric:4.0.2"
    const val guava = "com.google.guava:guava:22.0"
    const val junit = "junit:junit:4.12"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:3.12.0"
    const val mockito = "org.mockito:mockito-core:2.27.0"
}
