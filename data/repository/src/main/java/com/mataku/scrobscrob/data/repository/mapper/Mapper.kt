package com.mataku.scrobscrob.data.repository.mapper

import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.AlbumInfoTrack
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.ChartTopArtists
import com.mataku.scrobscrob.core.entity.ChartTopTracks
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.ChartTrackArtist
import com.mataku.scrobscrob.core.entity.Image
import com.mataku.scrobscrob.core.entity.LicenseArtifact
import com.mataku.scrobscrob.core.entity.NowPlayingTrackEntity
import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.Scm
import com.mataku.scrobscrob.core.entity.ScrobbleResult
import com.mataku.scrobscrob.core.entity.SpdxLicense
import com.mataku.scrobscrob.core.entity.Stats
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.TopAlbumInfo
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.TrackArtist
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.core.entity.UserInfo
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.core.entity.presentation.toReadableString
import com.mataku.scrobscrob.data.api.endpoint.TrackInfoApiResponse
import com.mataku.scrobscrob.data.api.model.AlbumInfoBody
import com.mataku.scrobscrob.data.api.model.AlbumInfoTrackBody
import com.mataku.scrobscrob.data.api.model.ArtistInfoApiResponse
import com.mataku.scrobscrob.data.api.model.ChartTopArtistsResponse
import com.mataku.scrobscrob.data.api.model.ChartTopTracksResponse
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.MultipleTag
import com.mataku.scrobscrob.data.api.model.PagingAttrBody
import com.mataku.scrobscrob.data.api.model.RecentTracksApiResponse
import com.mataku.scrobscrob.data.api.model.ScrobbleApiResponse
import com.mataku.scrobscrob.data.api.model.SingleTag
import com.mataku.scrobscrob.data.api.model.StatsBody
import com.mataku.scrobscrob.data.api.model.TagBody
import com.mataku.scrobscrob.data.api.model.TagListBody
import com.mataku.scrobscrob.data.api.model.TopAlbumsApiResponse
import com.mataku.scrobscrob.data.api.model.TrackAlbumInfoBody
import com.mataku.scrobscrob.data.api.model.TrackArtistBody
import com.mataku.scrobscrob.data.api.model.UserInfoApiResponse
import com.mataku.scrobscrob.data.api.model.UserTopArtistsApiResponse
import com.mataku.scrobscrob.data.api.model.WikiBody
import com.mataku.scrobscrob.data.db.entity.AppThemeEntity
import com.mataku.scrobscrob.data.db.entity.LicenseArtifactDefinitionEntity
import com.mataku.scrobscrob.data.db.entity.ScmEntity
import com.mataku.scrobscrob.data.db.entity.SpdxLicenseEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

fun ArtistInfoApiResponse.toArtistInfo(): ArtistInfo {
  val body = this.artistInfo
  val tagList = body.tags.toTagList().toImmutableList()

  return ArtistInfo(
    name = body.name,
    images = body.imageList?.toImageList()?.toImmutableList() ?: persistentListOf(),
    tags = tagList,
    stats = body.stats.toStats(),
    url = body.url,
    wiki = body.wiki.toWiki()
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
    name = body.name,
    playCount = body.playCount,
    wiki = body.wiki.toWiki()
  )
}

fun TopAlbumsApiResponse.toTopAlbums(): List<TopAlbumInfo> {
  val body = this.topAlbums
  return body.albums.map {
    TopAlbumInfo(
      artist = it.artist.name,
      title = it.name,
      imageList = it.imageList?.toImageList()?.toImmutableList() ?: persistentListOf(),
      playCount = it.playcount,
      url = it.url
    )
  }
}

fun UserTopArtistsApiResponse.toTopArtists(): List<TopArtistInfo> {
  val body = this.topArtists
  return body.artists.map {
    TopArtistInfo(
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

fun TagListBody?.toTagList(): List<Tag> {
  this ?: return emptyList()
  return when (this) {
    is SingleTag -> {
      listOf(
        Tag(name = this.name, url = "")
      )
    }

    is MultipleTag -> {
      this.tagList.toTagList()
    }

    else -> emptyList<Tag>()
  }
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

fun PagingAttrBody.toPagingAttr(): PagingAttr {
  return PagingAttr(
    page = this.page,
    perPage = this.perPage,
    total = this.total,
    totalPages = this.totalPages
  )
}

fun ChartTopArtistsResponse.toChartTopArtists(): ChartTopArtists {
  val body = this.chartTopArtistsBody
  val topArtists = body.topArtists.map { chartArtist ->
    ChartArtist(
      name = chartArtist.name,
      playCount = chartArtist.playCount,
      listeners = chartArtist.listeners,
      url = chartArtist.url,
      imageList = chartArtist.imageList.toImageList()
    )
  }
  val pagingAttr = body.pagingAttrBody.toPagingAttr()
  return ChartTopArtists(
    topArtists = topArtists,
    pagingAttr = pagingAttr
  )
}

fun ChartTopTracksResponse.toChartTopTracks(): ChartTopTracks {
  val body = this.chartTopTracksBody
  val topTracks = body.topTracks.map {
    ChartTrack(
      name = it.name,
      playCount = it.playCount,
      listeners = it.listeners,
      url = it.url,
      artist = ChartTrackArtist(
        name = it.artist.name,
        url = it.artist.url
      ),
      imageList = it.imageList.toImageList(),
      mbid = it.mbid
    )
  }
  val pagingAttr = body.pagingAttrBody.toPagingAttr()

  return ChartTopTracks(
    topTracks = topTracks,
    pagingAttr = pagingAttr
  )
}

fun ScmEntity?.toScm(): Scm? {
  this ?: return null
  return Scm(url = this.url)
}

fun List<SpdxLicenseEntity>.toSpdxLicenseList(): ImmutableList<SpdxLicense> {
  return this.map {
    SpdxLicense(
      identifier = it.identifier,
      name = it.name,
      url = it.url
    )
  }.toImmutableList()
}

fun List<LicenseArtifactDefinitionEntity>.toLicenseArtifactList(): List<LicenseArtifact> {
  return this.map {
    LicenseArtifact(
      artifactId = it.artifactId,
      groupId = it.groupId,
      name = it.name,
      scm = it.scm.toScm(),
      spdxLicenses = it.spdxLicenses.toSpdxLicenseList(),
      version = it.version
    )
  }
}

fun WikiBody?.toWiki(): Wiki? {
  this ?: return null

  return Wiki(
    published = this.published.toReadableString(),
    summary = this.summary,
    content = this.content
  )
}

fun List<TagBody>.toTagList(): List<Tag> {
  return this.map {
    Tag(
      name = it.name,
      url = it.url
    )
  }
}

fun AlbumInfoTrackBody?.toTrackList(): List<AlbumInfoTrack> {
  this ?: return emptyList()
  return this.tracks.map {
    AlbumInfoTrack(
      duration = it.duration,
      name = it.name,
      url = it.url
    )
  }
}

fun AlbumInfoBody.toAlbumInfo(): AlbumInfo {
  val tagList = this.tagsBody.toTagList().toImmutableList()
  return AlbumInfo(
    albumName = this.albumName,
    artistName = this.artistName,
    url = this.url,
    images = this.images.toImageList().toImmutableList(),
    listeners = this.listeners,
    playCount = this.playCount,
    tracks = this.tracks.toTrackList().toImmutableList(),
    tags = tagList,
    wiki = this.wiki.toWiki()
  )
}

fun StatsBody.toStats(): Stats {
  return Stats(
    listeners = this.listeners,
    playCount = this.playCount
  )
}

fun AppThemeEntity.toAppTheme(): AppTheme {
  return when (this) {
    AppThemeEntity.DARK -> AppTheme.DARK
    AppThemeEntity.LIGHT -> AppTheme.LIGHT
    AppThemeEntity.MIDNIGHT -> AppTheme.MIDNIGHT
    AppThemeEntity.OCEAN -> AppTheme.OCEAN
    AppThemeEntity.LASTFM_DARK -> AppTheme.LASTFM_DARK
  }
}

fun AppTheme.toAppThemeEntity(): AppThemeEntity {
  return when (this) {
    AppTheme.DARK -> AppThemeEntity.DARK
    AppTheme.LIGHT -> AppThemeEntity.LIGHT
    AppTheme.MIDNIGHT -> AppThemeEntity.MIDNIGHT
    AppTheme.OCEAN -> AppThemeEntity.OCEAN
    AppTheme.LASTFM_DARK -> AppThemeEntity.LASTFM_DARK
  }
}

fun UserInfoApiResponse.toUserInfo(): UserInfo {
  val body = this.userInfo
  return UserInfo(
    name = body.name,
    playCount = body.playCount,
    artistCount = body.artistCount,
    trackCount = body.trackCount,
    albumCount = body.albumCount,
    imageList = body.imageList?.toImageList()?.toImmutableList() ?: persistentListOf<Image>(),
    url = body.url
  )
}

