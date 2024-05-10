package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface ArtworkRepository {
//  suspend fun getArtwork(artist: String): Flow<String?>

  suspend fun artworkList(artist: List<String>): Flow<List<String?>>

  suspend fun insertArtwork(artist: String, artworkUrl: String)

  suspend fun deleteAll()

  suspend fun artworkListFromChartTrack(
    tracks: List<ChartTrack>
  )
}

@Singleton
class ArtworkRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val artworkDataStore: ArtworkDataStore
) : ArtworkRepository {
//  override suspend fun getArtwork(artist: String): Flow<String?> {
//    return artworkDataStore.artwork(artist).flowOn(Dispatchers.IO)
//  }

  override suspend fun artworkList(artist: List<String>): Flow<List<String?>> {
    return artworkDataStore.artworkList(artist).flowOn(Dispatchers.IO)
  }

  override suspend fun insertArtwork(artist: String, artworkUrl: String) {
    artworkDataStore.insertArtwork(artist, artworkUrl)
  }

  override suspend fun deleteAll() {
    artworkDataStore.deleteAll()
  }

  override suspend fun artworkListFromChartTrack(tracks: List<ChartTrack>) {

  }
}
