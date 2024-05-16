package com.mataku.scrobscrob.data.db

import android.content.Context
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mataku.scrobscrob.Database
import com.mataku.scrobscrob.data.db.entity.ArtistArtworkEntity
import com.mataku.scrobscrob.schema.artwork.SelectUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface ArtworkDataStore {
  suspend fun artwork(artist: String): Flow<String?>

  suspend fun artworkList(artists: List<String>): Flow<List<String?>>
  suspend fun artworkList2(artists: List<String>): List<ArtistArtworkEntity>
  suspend fun insertArtwork(albumName: String, artist: String, artworkUrl: String)
  suspend fun deleteAll()
}

@Singleton
class ArtworkDataStoreImpl @Inject constructor(
  context: Context
) : ArtworkDataStore {
  private val sqlDriver = AndroidSqliteDriver(Database.Schema, context, "scrobscrob.db")
  private val artworkQueries = Database(sqlDriver).artworkQueries

  override suspend fun artwork(artist: String): Flow<String?> {
    return artworkQueries.selectUrl(name = artist)
      .asFlow()
      .catch {
      }
      .mapToOneOrDefault(SelectUrl(""), Dispatchers.IO)
      .map {
        it.url
      }
  }

  override suspend fun deleteAll() {
    artworkQueries.deleteAll()
  }

  override suspend fun insertArtwork(albumName: String, artist: String, artworkUrl: String) {
    withContext(Dispatchers.IO) {
      artworkQueries.insert(
        name = artist,
        albumName = albumName,
        url = artworkUrl
      )
    }
  }

  override suspend fun artworkList(artists: List<String>): Flow<List<String?>> {
    return artworkQueries.selectUrlList(artists)
      .asFlow()
      .catch {
      }
      .map {
        it.executeAsList()
      }
      .map { selectUrlList ->
        selectUrlList.map { selectUrl ->
          selectUrl.url ?: ""
        }
      }
      .flowOn(Dispatchers.IO)
  }

  override suspend fun artworkList2(artists: List<String>): List<ArtistArtworkEntity> {
    return withContext(Dispatchers.IO) {
      artworkQueries.selectUrlList(artists).executeAsList()
        .map {
          ArtistArtworkEntity(
            name = it.name,
            url = it.url ?: ""
          )
        }
    }
  }
}
