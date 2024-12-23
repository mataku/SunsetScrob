package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.Image
import com.mataku.scrobscrob.core.entity.LovedTrack
import com.mataku.scrobscrob.core.entity.UserInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.core.entity.isInvalidArtwork
import com.mataku.scrobscrob.data.api.BuildConfig
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.UserInfoEndpoint
import com.mataku.scrobscrob.data.api.endpoint.UserLovedTracksEndpoint
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import com.mataku.scrobscrob.data.repository.mapper.toRecentTrackList
import com.mataku.scrobscrob.data.repository.mapper.toUserInfo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
  suspend fun getInfo(userName: String): Flow<UserInfo>
  suspend fun getLovedTracks(page: Int): Result<List<LovedTrack>>
}

@Singleton
class UserRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val usernameDataStore: UsernameDataStore,
  private val artworkDataStore: ArtworkDataStore,
) : UserRepository {
  override suspend fun getInfo(userName: String): Flow<UserInfo> = flow {
    val endpoint = UserInfoEndpoint(
      params = mapOf(
        "user" to userName
      )
    )
    val userInfo = if (BuildConfig.DEBUG) {
      UserInfo(
        name = "matakucom",
        playCount = "102654",
        artistCount = "728",
        trackCount = "2296",
        albumCount = "1753",
        imageList = persistentListOf(
          Image(
            size = "extralarge",
            url = "https://lastfm.freetls.fastly.net/i/u/300x300/3605caa7a395e19202c55d55be23cbff.png"
          )
        ),
        url = "https://www.last.fm/user/matakucom"
      )
    } else {
      lastFmService.request(endpoint).toUserInfo()
    }
    emit(userInfo)
  }.flowOn(Dispatchers.IO)

  override suspend fun getLovedTracks(page: Int): Result<List<LovedTrack>> {
    return withContext(Dispatchers.IO) {
      val username = usernameDataStore.username()
      if (username.isNullOrEmpty()) {
        return@withContext Result.success(emptyList())
      }
      val endpoint = UserLovedTracksEndpoint(
        params = mapOf(
          "limit" to "20",
          "page" to page.toString(),
          "user" to username
        )
      )
      runCatching {
        lastFmService.request(endpoint).toRecentTrackList().map { track ->
          if (track.images.imageUrl().isInvalidArtwork()) {
            val imageUrl = artworkDataStore.artwork(
              artist = track.artist,
            )
            if (imageUrl != null) {
              track.imageUrl = imageUrl
            }
          }
          track
        }
      }
    }
  }
}
