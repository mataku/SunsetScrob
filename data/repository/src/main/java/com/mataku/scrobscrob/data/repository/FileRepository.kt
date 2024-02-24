package com.mataku.scrobscrob.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import javax.inject.Inject
import javax.inject.Singleton

interface FileRepository {
  suspend fun cacheImageDirMBSize(): Double

  suspend fun deleteCacheImageDir()
}

@Singleton
class FileRepositoryImpl @Inject constructor(
  @ApplicationContext private val applicationContext: Context
) : FileRepository {
  override suspend fun cacheImageDirMBSize(): Double {
    return withContext(Dispatchers.IO) {
      val imageCacheDir = applicationContext.cacheDir.resolve("sunsetscrob_image")
      runCatching {
        val bytes = Files.size(imageCacheDir.toPath())
        bytes / (1024.0 * 1024.0)
      }.getOrNull() ?: 0.0
    }
  }

  override suspend fun deleteCacheImageDir() {
    val imageCacheDir = applicationContext.cacheDir.resolve("sunsetscrob_image")
    runCatching {
      if (imageCacheDir.exists()) {
        imageCacheDir.deleteRecursively()
      }
    }
  }
}
