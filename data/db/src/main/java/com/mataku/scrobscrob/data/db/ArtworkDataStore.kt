package com.mataku.scrobscrob.data.db

import android.content.Context
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mataku.scrobscrob.Database
import com.mataku.scrobscrob.schema.SelectUrl
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

  suspend fun artworkList(artist: List<String>): Flow<List<String?>>
  suspend fun insertArtwork(artist: String, artworkUrl: String)
  suspend fun deleteAll()
}

@Singleton
class ArtworkDataStoreImpl @Inject constructor(
  context: Context
) : ArtworkDataStore {
  private val sqlDriver = AndroidSqliteDriver(Database.Schema, context, "scrobscrob.db")
  private val artistArtworkQueries = Database(sqlDriver).artistArtworkQueries

  override suspend fun artwork(artist: String): Flow<String?> {
    println("MATAKUDEBUG call artwork $artist")
    return artistArtworkQueries.selectUrl(name = artist)
      .asFlow()
      .catch {
        println("MATAKUDEBUG error artwork $it")
      }
      .mapToOneOrDefault(SelectUrl(""), Dispatchers.IO)
      .map {
        println("MATAKUDEBUG artwork $it")
        it.url
      }
  }

  override suspend fun insertArtwork(artist: String, artworkUrl: String) {
    withContext(Dispatchers.IO) {
      artistArtworkQueries.insert(
        name = artist,
        url = artworkUrl
      )
    }
  }

  override suspend fun artworkList(artist: List<String>): Flow<List<String?>> {
    return artistArtworkQueries.selectUrlList(artist)
      .asFlow()
      .catch {
        println("MATAKUDEBUG error $it")
      }
      .map {
        it.executeAsList()
      }
      .map { selectUrlList ->
        println("MATAKUDEBUG artwork List $selectUrlList")
        selectUrlList.map { selectUrl ->
          selectUrl.url ?: ""
        }
      }
      .flowOn(Dispatchers.IO)
  }

  override suspend fun deleteAll() {
    artistArtworkQueries.deleteAll()
  }
}
