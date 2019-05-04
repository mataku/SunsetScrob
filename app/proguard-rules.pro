-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*, InnerClasses
-keepattributes Signature, Exception
-keepnames class ** { *; }

-keep class kotlinx.** { *; }
-keep interface kotlinx.** { *; }
-keep class kotlin.** { *; }
-keep interface kotlin.** { *; }

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

# RxJava
-dontwarn rx.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder