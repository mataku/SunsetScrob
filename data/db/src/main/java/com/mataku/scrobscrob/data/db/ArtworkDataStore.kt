package com.mataku.scrobscrob.data.db

import android.content.Context
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mataku.scrobscrob.Database
import com.mataku.scrobscrob.data.db.entity.ArtistArtworkEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

interface ArtworkDataStore {
  suspend fun artwork(artist: String): String?
  suspend fun albumArtwork(albumName: String, artist: String): String?

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

  override suspend fun artwork(artist: String): String? {
    return withContext(Dispatchers.IO) {
      artworkQueries.selectUrl(name = artist)
        .executeAsOneOrNull()?.url
    }
  }

  override suspend fun deleteAll() {
    artworkQueries.deleteAll()
  }

  override suspend fun insertArtwork(albumName: String, artist: String, artworkUrl: String) {
    if (artworkUrl.isInvalidArtwork()) return
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

  override suspend fun albumArtwork(albumName: String, artist: String): String? {
    return withContext(Dispatchers.IO) {
      artworkQueries.selectAlbumUrl(
        name = artist,
        album_name = albumName
      ).executeAsOneOrNull()?.url
    }
  }
}

@OptIn(ExperimentalContracts::class)
private fun String?.isInvalidArtwork(): Boolean {
  contract {
    returns(false) implies (this@isInvalidArtwork != null)
  }

  this ?: return true

  val lastElement = this.split("/").lastOrNull()?.split(".")?.get(0) ?: return true
  return lastElement == "2a96cbd8b46e442fc41c2b86b821562f"
}

