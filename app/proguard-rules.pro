-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*, InnerClasses
-keepattributes Signature, Exception

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

# coil
-keep class * extends coil3.util.DecoderServiceLoaderTarget { *; }
-keep class * extends coil3.util.FetcherServiceLoaderTarget { *; }
