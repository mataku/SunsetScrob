package com.mataku.scrobscrob.ui_common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class SunsetGlideModule : AppGlideModule() {
  override fun applyOptions(context: Context, builder: GlideBuilder) {
    val cacheSizeBytes = 1024L * 1024L * 256L // 256MB
    builder
      .setMemoryCache(LruResourceCache(cacheSizeBytes))
      .setDiskCache(
        InternalCacheDiskCacheFactory(context, "sunsetscrob_image", cacheSizeBytes)
      )
      .setDefaultTransitionOptions(
        Drawable::class.java, DrawableTransitionOptions.withCrossFade(300)
      )
      .setDefaultTransitionOptions(
        Bitmap::class.java, BitmapTransitionOptions.withCrossFade(300)
      )
  }
}
