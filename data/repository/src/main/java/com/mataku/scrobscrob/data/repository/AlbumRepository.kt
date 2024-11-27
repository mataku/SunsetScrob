package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.AlbumInfoEndpoint
import com.mataku.scrobscrob.data.api.endpoint.UserTopAlbumsEndpoint
import com.mataku.scrobscrob.data.api.model.TopAlbumsApiResponse
import com.mataku.scrobscrob.data.repository.mapper.toAlbumInfo
import com.mataku.scrobscrob.data.repository.mapper.toTopAlbums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface AlbumRepository {
  suspend fun fetchTopAlbums(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<List<TopAlbumInfo>>

  fun albumInfo(
    albumName: String,
    artistName: String,
  ): Flow<AlbumInfo>
}

private val topALbumsJson = """
  {
    "topalbums": {
      "album": [
        {
          "name": "真っ白なものは汚したくなる (Complete Edition)",
          "playcount": "1989",
          "mbid": "",
          "url": "https://www.last.fm/music/%E6%AC%85%E5%9D%8246/%E7%9C%9F%E3%81%A3%E7%99%BD%E3%81%AA%E3%82%82%E3%81%AE%E3%81%AF%E6%B1%9A%E3%81%97%E3%81%9F%E3%81%8F%E3%81%AA%E3%82%8B+(Complete+Edition)",
          "artist": {
            "name": "欅坂46",
            "mbid": "",
            "url": "https://www.last.fm/music/%E6%AC%85%E5%9D%8246"
          },
          "image": [
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/34s/42f3bac9af3bdfe372ea42e21157aad0.png",
              "size": "small"
            },
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/64s/42f3bac9af3bdfe372ea42e21157aad0.png",
              "size": "medium"
            },
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/174s/42f3bac9af3bdfe372ea42e21157aad0.png",
              "size": "large"
            },
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/42f3bac9af3bdfe372ea42e21157aad0.png",
              "size": "extralarge"
            }
          ],
          "@attr": {
            "rank": "1"
          }
        },
        {
          "name": "ZENITH",
          "playcount": "1791",
          "mbid": "",
          "url": "https://www.last.fm/music/PassCode/ZENITH",
          "artist": {
            "name": "PassCode",
            "mbid": "",
            "url": "https://www.last.fm/music/PassCode"
          },
          "image": [
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/34s/580662fd9ffce7415f00c9130fc3d816.png",
              "size": "small"
            },
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/64s/580662fd9ffce7415f00c9130fc3d816.png",
              "size": "medium"
            },
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/174s/580662fd9ffce7415f00c9130fc3d816.png",
              "size": "large"
            },
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/580662fd9ffce7415f00c9130fc3d816.png",
              "size": "extralarge"
            }
          ],
          "@attr": {
            "rank": "2"
          }
        },
        {
          "name": "Virtual",
          "playcount": "1320",
          "mbid": "",
          "url": "https://www.last.fm/music/PassCode/Virtual",
          "artist": {
            "name": "PassCode",
            "mbid": "",
            "url": "https://www.last.fm/music/PassCode"
          },
          "image": [
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/34s/255eb4751ad6d52f31a0a0889a06055f.png",
              "size": "small"
            },
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/64s/255eb4751ad6d52f31a0a0889a06055f.png",
              "size": "medium"
            },
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/174s/255eb4751ad6d52f31a0a0889a06055f.png",
              "size": "large"
            },
            {
              "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/255eb4751ad6d52f31a0a0889a06055f.png",
              "size": "extralarge"
            }
          ],
          "@attr": {
            "rank": "3"
          }
        }
      ],
      "@attr": {
        "user": "rhythnn",
        "page": "1",
        "perPage": "3",
        "totalPages": "66",
        "total": "197"
      }
    }
  }
""".trimIndent()

@Singleton
class AlbumRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService
) : AlbumRepository {
  override suspend fun fetchTopAlbums(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<List<TopAlbumInfo>> = flow {
    if (true) {
      emit(TopAlbumsApiResponse.parse(topALbumsJson).toTopAlbums())
      return@flow
    }

    val params = mapOf(
      "limit" to 20,
      "page" to page,
      "period" to timeRangeFiltering.rawValue,
      "user" to username
    )
    val endpoint = UserTopAlbumsEndpoint(
      params = params
    )
    val response = lastFmService.request(endpoint)
    emit(response.toTopAlbums())
  }.flowOn(Dispatchers.IO)

  override fun albumInfo(
    albumName: String,
    artistName: String
  ): Flow<AlbumInfo> = flow<AlbumInfo> {
    val params = mapOf(
      "album" to albumName,
      "artist" to artistName
    )
    val endpoint = AlbumInfoEndpoint(params = params)
    val response = lastFmService.request(endpoint)
    emit(response.albumInfoBody.toAlbumInfo())
  }.flowOn(Dispatchers.IO)
}
