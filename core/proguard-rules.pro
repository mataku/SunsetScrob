-keepattributes *Annotation*, InnerClasses
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature, Exception
-keepnames class ** { *; }
-keep class com.mataku.scrobscrob.core.api.endpoint.** { *; }

-keep class kotlinx.** { *; }
-keep interface kotlinx.** { *; }
-keep class kotlin.** { *; }
-keep interface kotlin.** { *; }

# Kotlin serialization
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.mataku.scrobscrob.core.**$$serializer { *; }
-keepclassmembers class com.mataku.scrobscrob.core.** {
    *** Companion;
}
-keepclasseswithmembers class com.mataku.scrobscrob.core.** {
    kotlinx.serialization.KSerializer serializer(...);
}

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

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder