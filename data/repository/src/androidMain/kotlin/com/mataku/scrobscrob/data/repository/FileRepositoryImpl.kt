package com.mataku.scrobscrob.data.repository

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.nio.file.Files

@SingleIn(AppScope::class)
@Inject
class FileRepositoryImpl(
  private val applicationContext: Context
) : FileRepository {
  override fun cacheImageDirMBSize(): Flow<Double> = flow {
    val imageCacheDir = applicationContext.cacheDir.resolve("sunsetscrob_image")
    val mb = runCatching {
      val bytes = Files.size(imageCacheDir.toPath())
      bytes / (1024.0 * 1024.0)
    }.getOrNull() ?: 0.0
    emit(mb)
  }.flowOn(Dispatchers.IO)

  override fun deleteCacheImageDir(): Flow<Unit> = flow {
    val imageCacheDir = applicationContext.cacheDir.resolve("sunsetscrob_image")
    runCatching {
      if (imageCacheDir.exists()) {
        imageCacheDir.deleteRecursively()
      }
    }
    emit(Unit)
  }.flowOn(Dispatchers.IO)
}
