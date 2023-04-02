package com.mataku.scrobscrob.data.repository.mapper

import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.Image
import com.mataku.scrobscrob.core.entity.NowPlaying
import com.mataku.scrobscrob.core.entity.NowPlayingTrack
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.ScrobbleResult
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.TrackArtist
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.data.api.endpoint.TrackInfoApiResponse
import com.mataku.scrobscrob.data.api.model.ArtistInfoApiResponse
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.NowPlayingApiResponse
import com.mataku.scrobscrob.data.api.model.RecentTracksApiResponse
import com.mataku.scrobscrob.data.api.model.ScrobbleApiResponse
import com.mataku.scrobscrob.data.api.model.TopTagsBody
import com.mataku.scrobscrob.data.api.model.TrackAlbumInfoBody
import com.mataku.scrobscrob.data.api.model.TrackArtistBody
import com.mataku.scrobscrob.data.api.model.UserTopAlbumsApiResponse
import com.mataku.scrobscrob.data.api.model.UserTopArtistsApiResponse
import com.mataku.scrobscrob.data.db.NowPlayingTrackEntity
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

fun ArtistInfoApiResponse.toArtistInfo(): ArtistInfo {
  val body = this.artistInfo
  return ArtistInfo(
    name = body.name,
    imageList = body.imageList?.toImageList()?.toImmutableList() ?: persistentListOf(),
    topTags = body.topTags.toTagList().toImmutableList(),
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
    imageList = this.imageList.toImageList().toImmutableList(),
  )
}

fun TrackArtistBody.toTrackArtist(): TrackArtist {
  return TrackArtist(
    name = name,
    url = url
  )
}

fun TrackInfoApiResponse.toTrackInfo(): TrackInfo {
  val body = this.trackInfo
  return TrackInfo(
    duration = body.duration,
    album = body.album?.toTrackAlbumInfo(),
    listeners = body.listeners,
    url = body.url,
    topTags = body.topTags.toTagList().toImmutableList(),
    artist = body.artist.toTrackArtist(),
    name = body.name
  )
}

fun UserTopAlbumsApiResponse.toTopAlbums(): List<AlbumInfo> {
  val body = this.topAlbums
  return body.albums.map {
    AlbumInfo(
      artist = it.artist.name,
      title = it.name,
      imageList = it.imageList?.toImageList()?.toImmutableList() ?: persistentListOf(),
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
      imageList = it.imageList?.toImageList()?.toImmutableList() ?: persistentListOf(),
      url = it.url,
      topTags = persistentListOf(),
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
      images = it.images.toImageList().toImmutableList(),
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

fun NowPlayingTrackEntity.toNowPlayingTrack(): NowPlayingTrack {
  return NowPlayingTrack(
    artistName = artistName,
    trackName = trackName,
    albumName = albumName,
    artwork = artwork,
    duration = duration
  )
}

fun TrackInfo.toNowPlayingTrackEntity(): NowPlayingTrackEntity {
  return NowPlayingTrackEntity(
    artistName = artist.name,
    trackName = name,
    albumName = album?.title ?: "",
    artwork = album?.imageList?.imageUrl() ?: "",
    duration = if (duration == 0L) {
      300000L
    } else {
      duration
    }
  )
}

fun ScrobbleApiResponse.toScrobbleResult(): ScrobbleResult {
  // TODO: multi track scrobble
  val body = this.scrobbleResult
  return ScrobbleResult(
    accepted = body.attr.accepted == 1
  )
}


