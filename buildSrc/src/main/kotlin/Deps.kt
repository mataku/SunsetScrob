const val kotlinVersion = "1.3.30"

object Deps {
    const val ktorVersion = "1.1.3"

    const val appCompat = "androidx.appcompat:appcompat:1.0.2"
    const val androidGradlePlugin = "com.android.tools.build:gradle:3.4.0-rc03"
    const val browser = "androidx.browser:browser:1.0.0"
    const val cardView = "androidx.cardview:cardview:1.0.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:2.9.5"

    const val epoxy = "com.airbnb.android:epoxy:3.5.0"
    const val epoxyDatabinding = "com.airbnb.android:epoxy-databinding:3.5.0"
    const val epoxyProcessor = "com.airbnb.android:epoxy-processor:3.5.0"

    const val firebaseCore = "com.google.firebase:firebase-core:16.0.1"

    const val glide = "com.github.bumptech.glide:glide:4.8.0"
    const val glideCompiler = "com.github.bumptech.glide:compiler:4.8.0"

    const val koinAndroid = "org.koin:koin-android:1.0.2"
    const val koinAndroidXScope = "org.koin:koin-androidx-scope:1.0.2"
    const val koinAndroidXViewModel = "org.koin:koin-androidx-viewmodel:1.0.2"

    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.1.1"
    const val kotlnGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"

    const val ktorClientOkhttp = "io.ktor:ktor-client-okhttp:$ktorVersion"
    const val ktorClientJsonJvm = "io.ktor:ktor-client-json-jvm:$ktorVersion"

    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.0.0"
    const val materialComponent = "com.google.android.material:material:1.0.0"

    const val okhttp = "com.squareup.okhttp3:okhttp:3.14.2"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:3.14.2"

    const val preference = "androidx.preference:preference:1.0.0"

    const val spek = "org.jetbrains.spek:spek-api:1.1.5"
    const val spekJunitPlatformEngine = "org.jetbrains.spek:spek-junit-platform-engine:1.1.5"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
    const val junitPlatformRunner = "org.junit.platform:junit-platform-runner:1.1.0"

    const val roomRuntime = "androidx.room:room-runtime:2.0.0"
    const val roomCompiler = "androidx.room:room-compiler:2.0.0"
    const val rxjava = "io.reactivex.rxjava2:rxjava:2.2.3"
}

object UnitTestDeps {
    const val androidxTestCore = "androidx.test:core:1.1.0"
    const val spek = "org.jetbrains.spek:spek-api:1.1.5"
    const val spekJunitPlatformEngine = "org.jetbrains.spek:spek-junit-platform-engine:1.1.5"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
    const val junitPlatformRunner = "org.junit.platform:junit-platform-runner:1.1.0"
    const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
    const val robolectric = "org.robolectric:robolectric:4.0.2"
    const val guava = "com.google.guava:guava:22.0"
    const val junit = "junit:junit:4.12"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:3.12.0"
    const val mockito = "org.mockito:mockito-core:2.27.0"
}