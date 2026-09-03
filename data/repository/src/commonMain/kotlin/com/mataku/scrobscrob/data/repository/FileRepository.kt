package com.mataku.scrobscrob.data.repository

import kotlinx.coroutines.flow.Flow

interface FileRepository {
  fun cacheImageDirMBSize(): Flow<Double>

  fun deleteCacheImageDir(): Flow<Unit>
}
