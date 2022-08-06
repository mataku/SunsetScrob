package com.mataku.scrobscrob.data.repository.mapper

import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.Image
import com.mataku.scrobscrob.core.entity.NowPlaying
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.data.api.endpoint.TrackInfoApiResponse
import com.mataku.scrobscrob.data.api.model.ArtistInfoApiResponse
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.NowPlayingApiResponse
import com.mataku.scrobscrob.data.api.model.RecentTracksApiResponse
import com.mataku.scrobscrob.data.api.model.TopTagsBody
import com.mataku.scrobscrob.data.api.model.TrackAlbumInfoBody
import com.mataku.scrobscrob.data.api.model.UserTopAlbumsApiResponse
import com.mataku.scrobscrob.data.api.model.UserTopArtistsApiResponse

fun ArtistInfoApiResponse.toArtistInfo(): ArtistInfo {
  val body = this.artistInfo
  return ArtistInfo(
    name = body.name,
    imageList = body.imageList?.toImageList() ?: emptyList(),
    topTags = body.topTags.toTagList(),
    playCount = body.stats.playCount,
    url = body.url
  )
}

fun List<ImageBody>.toImageList(): List<Image> {
  return this.map {
    Image(
      size = it.size,
      url = it.url
    )
  }
}

fun TrackAlbumInfoBody.toTrackAlbumInfo(): TrackAlbumInfo {
  return TrackAlbumInfo(
    artist = this.artist,
    title = this.title,
    imageList = this.imageList.toImageList(),
  )
}

fun TrackInfoApiResponse.toTrackInfo(): TrackInfo {
  val body = this.trackInfo
  return TrackInfo(
    duration = body.duration,
    album = body.album?.toTrackAlbumInfo(),
    listeners = body.listeners,
    url = body.url,
    topTags = body.topTags.toTagList()
  )
}

fun UserTopAlbumsApiResponse.toTopAlbums(): List<AlbumInfo> {
  val body = this.topAlbums
  return body.albums.map {
    AlbumInfo(
      artist = it.artist.name,
      title = it.name,
      imageList = it.imageList?.toImageList() ?: emptyList(),
      playCount = it.playcount,
      url = it.url
    )
  }
}

fun UserTopArtistsApiResponse.toTopArtists(): List<ArtistInfo> {
  val body = this.topArtists
  return body.artists.map {
    ArtistInfo(
      name = it.name,
      imageList = it.imageList?.toImageList() ?: emptyList(),
      url = it.url,
      topTags = emptyList(),
      playCount = it.playcount
    )
  }
}

fun RecentTracksApiResponse.toRecentTracks(): List<RecentTrack> {
  val body = this.recentTracks
  return body.tracks.map {
    RecentTrack(
      artistName = it.artist.name,
      albumName = it.album.name,
      images = it.images.toImageList(),
      name = it.name,
      url = it.url,
      date = it.date?.date
    )
  }
}

fun NowPlayingApiResponse.toNowPlaying(): NowPlaying {
  val body = this.nowPlaying
  return NowPlaying(
    artistName = body.artist.text,
    trackName = body.track.text,
    albumName = body.album.text
  )
}

fun TopTagsBody.toTagList(): List<Tag> {
  return this.tagList.map {
    Tag(
      name = it.name,
      url = it.url
    )
  }
}

