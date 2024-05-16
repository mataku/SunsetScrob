package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

interface ArtworkRepository {
//  suspend fun getArtwork(artist: String): Flow<String?>
//
//  suspend fun artworkList(artist: List<String>): Flow<List<String?>>
//
//  suspend fun artworkList(tracks: List<ChartTrack>): Flow<List<ChartTrack>>
//
//  suspend fun insertArtwork(albumName: String, artist: String, artworkUrl: String?)
//
//  suspend fun deleteAll()
//
//  suspend fun artworkListFromChartTrack(
//    tracks: List<ChartTrack>
//  )
//
//  suspend fun insertArtworks(
//    recentTracks: List<RecentTrack>
//  )
}

@Singleton
class ArtworkRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val artworkDataStore: ArtworkDataStore
) : ArtworkRepository {
//  override suspend fun getArtwork(artist: String): Flow<String?> {
//    return artworkDataStore.artwork(artist).flowOn(Dispatchers.IO)
//  }
//
//  override suspend fun artworkList(artist: List<String>): Flow<List<String?>> {
//    return artworkDataStore.artworkList(artist).flowOn(Dispatchers.IO)
//  }
//
//  override suspend fun deleteAll() {
//    artworkDataStore.deleteAll()
//  }
//
//  override suspend fun artworkListFromChartTrack(tracks: List<ChartTrack>) {
//
//  }
//
//  override suspend fun insertArtwork(albumName: String, artist: String, artworkUrl: String?) {
//    if (artworkUrl.isInvalidArtwork()) {
//      return
//    }
//    artworkDataStore.insertArtwork(
//      albumName = albumName,
//      artist = artist,
//      artworkUrl = artworkUrl
//    )
//  }
//
//  override suspend fun insertArtworks(recentTracks: List<RecentTrack>) {
//    recentTracks.forEach { recentTrack ->
//      insertArtwork(
//        albumName = recentTrack.albumName,
//        artist = recentTrack.artistName,
//        artworkUrl = recentTrack.images.imageUrl()
//      )
//    }
//  }
//
//  override suspend fun artworkList(tracks: List<ChartTrack>): Flow<List<ChartTrack>> = flow {
//    val tracksCopy = tracks.toMutableList()
//    val artistArtworks = artworkDataStore.artworkList2(tracks.map { it.artist.name })
//      .flowOn(Dispatchers.IO).single()
//    if (artistArtworks.isEmpty()) {
//
//    }
//
//    tracksCopy.forEach {
//
//    }
//    emit(emptyList())
//
//  }
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
