package com.mataku.scrobscrob.data.repository.mapper

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.Image
import com.mataku.scrobscrob.core.entity.PagingAttr
import com.mataku.scrobscrob.core.entity.Scm
import com.mataku.scrobscrob.core.entity.SpdxLicense
import com.mataku.scrobscrob.core.entity.Stats
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.TrackArtist
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.data.api.endpoint.TrackInfoApiResponse
import com.mataku.scrobscrob.data.api.model.AlbumArtistBody
import com.mataku.scrobscrob.data.api.model.AlbumBody
import com.mataku.scrobscrob.data.api.model.AlbumInfoBody
import com.mataku.scrobscrob.data.api.model.AlbumInfoTrackBody
import com.mataku.scrobscrob.data.api.model.AlbumsBody
import com.mataku.scrobscrob.data.api.model.ArtistBody
import com.mataku.scrobscrob.data.api.model.ArtistInfoApiResponse
import com.mataku.scrobscrob.data.api.model.ArtistInfoBody
import com.mataku.scrobscrob.data.api.model.ChartArtist
import com.mataku.scrobscrob.data.api.model.ChartTagBody
import com.mataku.scrobscrob.data.api.model.ChartTopArtistsBody
import com.mataku.scrobscrob.data.api.model.ChartTopArtistsResponse
import com.mataku.scrobscrob.data.api.model.ChartTopTagsBody
import com.mataku.scrobscrob.data.api.model.ChartTopTagsResponse
import com.mataku.scrobscrob.data.api.model.ChartTopTracksBody
import com.mataku.scrobscrob.data.api.model.ChartTopTracksResponse
import com.mataku.scrobscrob.data.api.model.ChartTrack
import com.mataku.scrobscrob.data.api.model.ChartTrackArtist
import com.mataku.scrobscrob.data.api.model.IgnoredMessage
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.LovedTrackBody
import com.mataku.scrobscrob.data.api.model.LovedTracksBody
import com.mataku.scrobscrob.data.api.model.LovedTracksResponse
import com.mataku.scrobscrob.data.api.model.MultipleTag
import com.mataku.scrobscrob.data.api.model.PagingAttrBody
import com.mataku.scrobscrob.data.api.model.RecentTrack
import com.mataku.scrobscrob.data.api.model.RecentTrackAlbumBody
import com.mataku.scrobscrob.data.api.model.RecentTrackArtistBody
import com.mataku.scrobscrob.data.api.model.RecentTrackAttrBody
import com.mataku.scrobscrob.data.api.model.RecentTrackDateBody
import com.mataku.scrobscrob.data.api.model.RecentTracksApiResponse
import com.mataku.scrobscrob.data.api.model.RecentTracksBody
import com.mataku.scrobscrob.data.api.model.ScrobbleApiResponse
import com.mataku.scrobscrob.data.api.model.ScrobbleAttrBody
import com.mataku.scrobscrob.data.api.model.ScrobbleBody
import com.mataku.scrobscrob.data.api.model.ScrobbleResultBody
import com.mataku.scrobscrob.data.api.model.ScrobbleValueResult
import com.mataku.scrobscrob.data.api.model.SingleTag
import com.mataku.scrobscrob.data.api.model.StatsBody
import com.mataku.scrobscrob.data.api.model.TagBody
import com.mataku.scrobscrob.data.api.model.TagListBody
import com.mataku.scrobscrob.data.api.model.TopAlbumsApiResponse
import com.mataku.scrobscrob.data.api.model.TopArtistsBody
import com.mataku.scrobscrob.data.api.model.TrackAlbumInfoBody
import com.mataku.scrobscrob.data.api.model.TrackArtistBody
import com.mataku.scrobscrob.data.api.model.TrackInfoBody
import com.mataku.scrobscrob.data.api.model.UserInfoApiResponse
import com.mataku.scrobscrob.data.api.model.UserInfoBody
import com.mataku.scrobscrob.data.api.model.UserTopArtistsApiResponse
import com.mataku.scrobscrob.data.api.model.WikiBody
import com.mataku.scrobscrob.data.db.entity.AppThemeEntity
import com.mataku.scrobscrob.data.db.entity.LicenseArtifactDefinitionEntity
import com.mataku.scrobscrob.data.db.entity.ScmEntity
import com.mataku.scrobscrob.data.db.entity.SpdxLicenseEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.persistentListOf
import java.util.Calendar
import java.util.GregorianCalendar

private val largeImage = ImageBody(size = "large", url = "https://example.com/large.png")
private val extraLargeImage = ImageBody(size = "extralarge", url = "https://example.com/extralarge.png")

private val pagingAttrBody = PagingAttrBody(
  page = "2",
  perPage = "20",
  totalPages = "5",
  total = "100",
)
private val pagingAttr = PagingAttr(
  page = "2",
  perPage = "20",
  totalPages = "5",
  total = "100",
)

private val trackInfoBody = TrackInfoBody(
  duration = 214000L,
  album = TrackAlbumInfoBody(
    artist = "aespa",
    title = "Drama - The 4th Mini Album",
    imageList = listOf(largeImage, extraLargeImage),
  ),
  listeners = "120000",
  playCount = "1500000",
  url = "https://www.last.fm/music/aespa/_/Drama",
  topTags = MultipleTag(
    tagList = listOf(TagBody(name = "k-pop", url = "https://www.last.fm/tag/k-pop")),
  ),
  artist = TrackArtistBody(name = "aespa", url = "https://www.last.fm/music/aespa"),
  name = "Drama",
  wiki = null,
  userPlayCount = "42",
  userLoved = "1",
)

class MapperSpec : DescribeSpec({
  describe("List<ImageBody>.toImageList") {
    it("keeps size and url of every image") {
      listOf(largeImage, extraLargeImage).toImageList() shouldBe listOf(
        Image(size = "large", url = "https://example.com/large.png"),
        Image(size = "extralarge", url = "https://example.com/extralarge.png"),
      )
    }
  }

  describe("TagListBody?.toTagList") {
    it("maps null to an empty list") {
      val body: TagListBody? = null
      body.toTagList().shouldBeEmpty()
    }

    it("maps a SingleTag to one tag without url") {
      SingleTag(name = "k-pop").toTagList() shouldBe listOf(Tag(name = "k-pop", url = ""))
    }

    it("maps a MultipleTag to every tag with its url") {
      val body = MultipleTag(
        tagList = listOf(
          TagBody(name = "k-pop", url = "https://www.last.fm/tag/k-pop"),
          TagBody(name = "pop", url = "https://www.last.fm/tag/pop"),
        ),
      )
      body.toTagList() shouldBe listOf(
        Tag(name = "k-pop", url = "https://www.last.fm/tag/k-pop"),
        Tag(name = "pop", url = "https://www.last.fm/tag/pop"),
      )
    }
  }

  describe("WikiBody?.toWiki") {
    it("maps null to null") {
      val body: WikiBody? = null
      body.toWiki().shouldBeNull()
    }

    it("formats the published date as `dd MMMM yyyy`") {
      val body = WikiBody(
        published = GregorianCalendar(2024, Calendar.JANUARY, 15).time,
        summary = "summary",
        content = "content",
      )
      body.toWiki()?.let { wiki ->
        wiki.published shouldBe "15 January 2024"
        wiki.summary shouldBe "summary"
        wiki.content shouldBe "content"
      }
    }
  }

  describe("PagingAttrBody.toPagingAttr") {
    it("copies every field") {
      pagingAttrBody.toPagingAttr() shouldBe pagingAttr
    }
  }

  describe("StatsBody.toStats") {
    it("copies listeners and playcount") {
      StatsBody(listeners = "1", playCount = "2").toStats() shouldBe Stats(listeners = "1", playCount = "2")
    }
  }

  describe("ArtistInfoApiResponse.toArtistInfo") {
    it("maps images, tags, stats and wiki") {
      val response = ArtistInfoApiResponse(
        artistInfo = ArtistInfoBody(
          name = "Nayeon",
          imageList = listOf(largeImage),
          tags = MultipleTag(tagList = listOf(TagBody(name = "k-pop", url = "https://www.last.fm/tag/k-pop"))),
          stats = StatsBody(listeners = "384242", playCount = "15045020"),
          url = "https://www.last.fm/music/Nayeon",
          wiki = null,
        ),
      )
      response.toArtistInfo().let { info ->
        info.name shouldBe "Nayeon"
        info.images shouldBe persistentListOf(Image(size = "large", url = "https://example.com/large.png"))
        info.tags shouldBe persistentListOf(Tag(name = "k-pop", url = "https://www.last.fm/tag/k-pop"))
        info.stats shouldBe Stats(listeners = "384242", playCount = "15045020")
        info.url shouldBe "https://www.last.fm/music/Nayeon"
        info.wiki.shouldBeNull()
      }
    }

    it("maps a missing image list and missing tags to empty lists") {
      val response = ArtistInfoApiResponse(
        artistInfo = ArtistInfoBody(
          name = "Nayeon",
          imageList = null,
          tags = null,
          stats = StatsBody(listeners = "1", playCount = "1"),
          url = "https://www.last.fm/music/Nayeon",
        ),
      )
      response.toArtistInfo().let { info ->
        info.images.shouldBeEmpty()
        info.tags.shouldBeEmpty()
      }
    }
  }

  describe("TrackInfoApiResponse.toTrackInfo") {
    it("maps every field, reading userloved as a flag") {
      TrackInfoApiResponse(trackInfo = trackInfoBody).toTrackInfo() shouldBe TrackInfo(
        duration = 214000L,
        artist = TrackArtist(name = "aespa", url = "https://www.last.fm/music/aespa"),
        album = TrackAlbumInfo(
          artist = "aespa",
          title = "Drama - The 4th Mini Album",
          imageList = persistentListOf(
            Image(size = "large", url = "https://example.com/large.png"),
            Image(size = "extralarge", url = "https://example.com/extralarge.png"),
          ),
        ),
        listeners = "120000",
        playCount = "1500000",
        url = "https://www.last.fm/music/aespa/_/Drama",
        topTags = persistentListOf(Tag(name = "k-pop", url = "https://www.last.fm/tag/k-pop")),
        name = "Drama",
        wiki = null,
        userPlayCount = "42",
        userLoved = true,
      )
    }

    it("defaults a missing user playcount to 0 and anything but 1 to not loved") {
      val body = trackInfoBody.copy(album = null, topTags = null, userPlayCount = null, userLoved = "0")
      TrackInfoApiResponse(trackInfo = body).toTrackInfo().let { info ->
        info.album.shouldBeNull()
        info.topTags.shouldBeEmpty()
        info.userPlayCount shouldBe "0"
        info.userLoved shouldBe false
      }
    }
  }

  describe("TrackInfo.toNowPlayingTrackEntity") {
    it("prefers the extralarge artwork and keeps a non-zero duration") {
      val entity = TrackInfoApiResponse(trackInfo = trackInfoBody).toTrackInfo().toNowPlayingTrackEntity()
      entity.artistName shouldBe "aespa"
      entity.trackName shouldBe "Drama"
      entity.albumName shouldBe "Drama - The 4th Mini Album"
      entity.artwork shouldBe "https://example.com/extralarge.png"
      entity.duration shouldBe 214000L
    }

    it("falls back to 5 minutes and blank album fields when the track has no album or duration") {
      val body = trackInfoBody.copy(album = null, duration = 0L)
      val entity = TrackInfoApiResponse(trackInfo = body).toTrackInfo().toNowPlayingTrackEntity()
      entity.albumName shouldBe ""
      entity.artwork shouldBe ""
      entity.duration shouldBe 300000L
    }
  }

  describe("TopAlbumsApiResponse.toTopAlbums") {
    it("maps albums with their artist name and paging") {
      val response = TopAlbumsApiResponse(
        topAlbums = AlbumsBody(
          albums = listOf(
            AlbumBody(
              name = "ZENITH",
              url = "https://www.last.fm/music/PassCode/ZENITH",
              artist = AlbumArtistBody(name = "PassCode"),
              imageList = listOf(largeImage),
              playcount = "12",
            ),
            AlbumBody(
              name = "no image",
              url = "https://www.last.fm/music/PassCode/no+image",
              artist = AlbumArtistBody(name = "PassCode"),
              imageList = null,
              playcount = "1",
            ),
          ),
          pagingAttrBody = pagingAttrBody,
        ),
      )
      response.toTopAlbums().let { topAlbums ->
        topAlbums.pagingAttr shouldBe pagingAttr
        topAlbums.albums.size shouldBe 2
        topAlbums.albums[0].let { album ->
          album.artist shouldBe "PassCode"
          album.title shouldBe "ZENITH"
          album.imageList shouldBe persistentListOf(Image(size = "large", url = "https://example.com/large.png"))
          album.playCount shouldBe "12"
          album.url shouldBe "https://www.last.fm/music/PassCode/ZENITH"
        }
        topAlbums.albums[1].imageList.shouldBeEmpty()
      }
    }
  }

  describe("UserTopArtistsApiResponse.toTopArtists") {
    it("maps artists with empty top tags and paging") {
      val response = UserTopArtistsApiResponse(
        topArtists = TopArtistsBody(
          artists = listOf(
            ArtistBody(
              name = "PassCode",
              url = "https://www.last.fm/music/PassCode",
              playcount = "300",
              imageList = listOf(largeImage),
            ),
            ArtistBody(
              name = "no image",
              url = "https://www.last.fm/music/no+image",
              playcount = "1",
              imageList = null,
            ),
          ),
          pagingAttrBody = pagingAttrBody,
        ),
      )
      response.toTopArtists().let { topArtists ->
        topArtists.pagingAttr shouldBe pagingAttr
        topArtists.artists[0].let { artist ->
          artist.name shouldBe "PassCode"
          artist.url shouldBe "https://www.last.fm/music/PassCode"
          artist.playCount shouldBe "300"
          artist.imageList shouldBe persistentListOf(Image(size = "large", url = "https://example.com/large.png"))
          artist.topTags.shouldBeEmpty()
        }
        topArtists.artists[1].imageList.shouldBeEmpty()
      }
    }
  }

  describe("RecentTracksApiResponse.toRecentTracks") {
    it("maps tracks, keeping a now-playing track's date null, and only totalPages of paging") {
      val response = RecentTracksApiResponse(
        recentTracks = RecentTracksBody(
          tracks = listOf(
            RecentTrack(
              artist = RecentTrackArtistBody(name = "TRACE"),
              images = listOf(largeImage),
              album = RecentTrackAlbumBody(name = "Supernova"),
              name = "Supernova",
              url = "https://www.last.fm/music/TRACE/_/Supernova",
              date = RecentTrackDateBody(date = "18 Oct 2026, 10:00"),
            ),
            RecentTrack(
              artist = RecentTrackArtistBody(name = "TRACE"),
              images = emptyList(),
              album = RecentTrackAlbumBody(name = ""),
              name = "Now playing",
              url = "https://www.last.fm/music/TRACE/_/Now+playing",
              date = null,
            ),
          ),
          attr = RecentTrackAttrBody(user = "smoke_user", totalPages = "3"),
        ),
      )
      response.toRecentTracks().let { recentTracks ->
        recentTracks.pagingAttr shouldBe PagingAttr(totalPages = "3")
        recentTracks.tracks[0].let { track ->
          track.artistName shouldBe "TRACE"
          track.albumName shouldBe "Supernova"
          track.images shouldBe persistentListOf(Image(size = "large", url = "https://example.com/large.png"))
          track.name shouldBe "Supernova"
          track.url shouldBe "https://www.last.fm/music/TRACE/_/Supernova"
          track.date shouldBe "18 Oct 2026, 10:00"
        }
        recentTracks.tracks[1].date.shouldBeNull()
      }
    }
  }

  describe("LovedTracksResponse.toRecentTrackList") {
    it("maps every loved track with its artist name and date") {
      val response = LovedTracksResponse(
        lovedTracks = LovedTracksBody(
          tracks = listOf(
            LovedTrackBody(
              artist = TrackArtistBody(name = "aespa", url = "https://www.last.fm/music/aespa"),
              images = listOf(largeImage),
              name = "Drama",
              url = "https://www.last.fm/music/aespa/_/Drama",
              date = RecentTrackDateBody(date = "18 Oct 2026, 10:00"),
            ),
          ),
          attr = pagingAttrBody,
        ),
      )
      response.toRecentTrackList().single().let { track ->
        track.artist shouldBe "aespa"
        track.images shouldBe persistentListOf(Image(size = "large", url = "https://example.com/large.png"))
        track.name shouldBe "Drama"
        track.url shouldBe "https://www.last.fm/music/aespa/_/Drama"
        track.date shouldBe "18 Oct 2026, 10:00"
      }
    }
  }

  describe("ScrobbleApiResponse.toScrobbleResult") {
    fun response(accepted: Int?) = ScrobbleApiResponse(
      scrobbleResult = ScrobbleResultBody(
        attr = ScrobbleAttrBody(accepted = accepted, ignored = 0),
        scrobble = ScrobbleBody(
          artist = ScrobbleValueResult(corrected = "0", name = "aespa"),
          ignoredMessage = IgnoredMessage(code = "0", message = ""),
          timestamp = "1760781600",
          album = ScrobbleValueResult(corrected = "0", name = "Drama"),
          track = ScrobbleValueResult(corrected = "0", name = "Drama"),
        ),
      ),
    )

    it("is accepted only when the API accepted exactly one scrobble") {
      response(accepted = 1).toScrobbleResult().accepted shouldBe true
      response(accepted = 0).toScrobbleResult().accepted shouldBe false
      response(accepted = null).toScrobbleResult().accepted shouldBe false
    }
  }

  describe("ChartTopArtistsResponse.toChartTopArtists") {
    it("maps artists and paging") {
      val response = ChartTopArtistsResponse(
        chartTopArtistsBody = ChartTopArtistsBody(
          topArtists = listOf(
            ChartArtist(
              name = "The Weeknd",
              playCount = "100",
              listeners = "50",
              url = "https://www.last.fm/music/The+Weeknd",
              imageList = listOf(largeImage),
            ),
          ),
          pagingAttrBody = pagingAttrBody,
        ),
      )
      response.toChartTopArtists().let { chart ->
        chart.pagingAttr shouldBe pagingAttr
        chart.topArtists.single().let { artist ->
          artist.name shouldBe "The Weeknd"
          artist.playCount shouldBe "100"
          artist.listeners shouldBe "50"
          artist.url shouldBe "https://www.last.fm/music/The+Weeknd"
          artist.imageList shouldBe listOf(Image(size = "large", url = "https://example.com/large.png"))
        }
      }
    }
  }

  describe("ChartTopTracksResponse.toChartTopTracks") {
    it("maps tracks with their artist, mbid and paging") {
      val response = ChartTopTracksResponse(
        chartTopTracksBody = ChartTopTracksBody(
          topTracks = listOf(
            ChartTrack(
              name = "My Love Mine All Mine",
              playCount = "100",
              listeners = "50",
              url = "https://www.last.fm/music/Mitski/_/My+Love+Mine+All+Mine",
              artist = ChartTrackArtist(name = "Mitski", url = "https://www.last.fm/music/Mitski"),
              imageList = listOf(largeImage),
              mbid = "mbid",
            ),
          ),
          pagingAttrBody = pagingAttrBody,
        ),
      )
      response.toChartTopTracks().let { chart ->
        chart.pagingAttr shouldBe pagingAttr
        chart.topTracks.single().let { track ->
          track.name shouldBe "My Love Mine All Mine"
          track.playCount shouldBe "100"
          track.listeners shouldBe "50"
          track.url shouldBe "https://www.last.fm/music/Mitski/_/My+Love+Mine+All+Mine"
          track.artist.name shouldBe "Mitski"
          track.artist.url shouldBe "https://www.last.fm/music/Mitski"
          track.imageList shouldBe listOf(Image(size = "large", url = "https://example.com/large.png"))
          track.mbid shouldBe "mbid"
        }
      }
    }
  }

  describe("ChartTopTagsResponse.toChartTopTags") {
    it("maps every tag with its url") {
      val response = ChartTopTagsResponse(
        chartTopTagsBody = ChartTopTagsBody(
          tagList = listOf(ChartTagBody(name = "rock", url = "https://www.last.fm/tag/rock")),
        ),
      )
      response.toChartTopTags() shouldBe persistentListOf(Tag(name = "rock", url = "https://www.last.fm/tag/rock"))
    }
  }

  describe("AlbumInfoBody.toAlbumInfo") {
    it("maps images, tracks, tags and wiki") {
      val body = AlbumInfoBody(
        albumName = "Drama",
        artistName = "aespa",
        url = "https://www.last.fm/music/aespa/Drama",
        images = listOf(largeImage),
        listeners = "10",
        playCount = "20",
        tracks = AlbumInfoTrackBody(
          tracks = listOf(
            AlbumInfoTrackBody.AlbumInfoTrackEntity(
              duration = "214",
              url = "https://www.last.fm/music/aespa/_/Drama",
              name = "Drama",
            ),
          ),
        ),
        tagsBody = SingleTag(name = "k-pop"),
        wiki = null,
      )
      body.toAlbumInfo().let { info ->
        info.albumName shouldBe "Drama"
        info.artistName shouldBe "aespa"
        info.url shouldBe "https://www.last.fm/music/aespa/Drama"
        info.images shouldBe persistentListOf(Image(size = "large", url = "https://example.com/large.png"))
        info.listeners shouldBe "10"
        info.playCount shouldBe "20"
        info.tracks.single().let { track ->
          track.duration shouldBe "214"
          track.url shouldBe "https://www.last.fm/music/aespa/_/Drama"
          track.name shouldBe "Drama"
        }
        info.tags shouldBe persistentListOf(Tag(name = "k-pop", url = ""))
        info.wiki.shouldBeNull()
      }
    }

    it("maps missing tracks and tags to empty lists") {
      val body = AlbumInfoBody(
        albumName = "Drama",
        artistName = "aespa",
        url = "https://www.last.fm/music/aespa/Drama",
        images = emptyList(),
        listeners = "10",
        playCount = "20",
      )
      body.toAlbumInfo().let { info ->
        info.tracks.shouldBeEmpty()
        info.tags.shouldBeEmpty()
      }
    }
  }

  describe("UserInfoApiResponse.toUserInfo") {
    it("maps every field") {
      val response = UserInfoApiResponse(
        userInfo = UserInfoBody(
          name = "smoke_user",
          playCount = "1",
          artistCount = "2",
          trackCount = "3",
          albumCount = "4",
          imageList = listOf(largeImage),
          url = "https://www.last.fm/user/smoke_user",
        ),
      )
      response.toUserInfo().let { info ->
        info.name shouldBe "smoke_user"
        info.playCount shouldBe "1"
        info.artistCount shouldBe "2"
        info.trackCount shouldBe "3"
        info.albumCount shouldBe "4"
        info.imageList shouldBe persistentListOf(Image(size = "large", url = "https://example.com/large.png"))
        info.url shouldBe "https://www.last.fm/user/smoke_user"
      }
    }

    it("maps a missing image list to an empty list") {
      val response = UserInfoApiResponse(
        userInfo = UserInfoBody(
          name = "smoke_user",
          playCount = "1",
          artistCount = "2",
          trackCount = "3",
          albumCount = "4",
          imageList = null,
          url = "https://www.last.fm/user/smoke_user",
        ),
      )
      response.toUserInfo().imageList.shouldBeEmpty()
    }
  }

  describe("List<LicenseArtifactDefinitionEntity>.toLicenseArtifactList") {
    it("maps scm and spdx licenses, keeping a missing scm null") {
      val entities = listOf(
        LicenseArtifactDefinitionEntity(
          artifactId = "ktor-client-core",
          groupId = "io.ktor",
          name = "Ktor",
          scm = ScmEntity(url = "https://github.com/ktorio/ktor"),
          spdxLicenses = listOf(
            SpdxLicenseEntity(
              identifier = "Apache-2.0",
              name = "Apache License 2.0",
              url = "https://www.apache.org/licenses/LICENSE-2.0",
            ),
          ),
          version = "3.6.0",
        ),
        LicenseArtifactDefinitionEntity(
          artifactId = "no-scm",
          groupId = "com.example",
          version = "1.0.0",
        ),
      )
      entities.toLicenseArtifactList().let { artifacts ->
        artifacts[0].let { artifact ->
          artifact.artifactId shouldBe "ktor-client-core"
          artifact.groupId shouldBe "io.ktor"
          artifact.name shouldBe "Ktor"
          artifact.scm shouldBe Scm(url = "https://github.com/ktorio/ktor")
          artifact.spdxLicenses shouldBe persistentListOf(
            SpdxLicense(
              identifier = "Apache-2.0",
              name = "Apache License 2.0",
              url = "https://www.apache.org/licenses/LICENSE-2.0",
            ),
          )
          artifact.version shouldBe "3.6.0"
        }
        artifacts[1].let { artifact ->
          artifact.name shouldBe ""
          artifact.scm.shouldBeNull()
          artifact.spdxLicenses.shouldBeEmpty()
        }
      }
    }
  }

  describe("AppThemeEntity <-> AppTheme") {
    it("round-trips every theme") {
      AppThemeEntity.entries.forEach { entity ->
        entity.toAppTheme().toAppThemeEntity() shouldBe entity
      }
      AppTheme.entries.forEach { theme ->
        theme.toAppThemeEntity().toAppTheme() shouldBe theme
      }
    }
  }
})
