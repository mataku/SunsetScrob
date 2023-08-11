-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*, InnerClasses
-keepattributes Signature, Exception
-keepnames class ** { *; }

-keep class kotlin.reflect.** { *; }
-keep interface kotlin.reflect.** { *; }

# Kotlin serialization
-dontnote kotlinx.serialization.SerializationKt
-dontnote kotlinx.serialization.Serialization
-keep,includedescriptorclasses class com.mataku.scrobscrob.**$$serializer { *; }
-keepclassmembers class com.mataku.scrobscrob.** {
    *** Companion;
}
-keepclasseswithmembers class com.mataku.scrobscrob.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Material components
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }

# AndroidX
-dontwarn androidx.**
-keep class androidx.** { *; }
-keepclassmembers class androidx.** { *; }
-keep interface androidx.* { *; }

# Okhttp
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.* { *; }

# Ktor
-keep class io.ktor.** { *; }
-dontwarn kotlinx.atomicfu.**
-dontwarn io.netty.**
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**

-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

-keep public class * extends com.airbnb.android.showkase.models.ShowkaseProvider
