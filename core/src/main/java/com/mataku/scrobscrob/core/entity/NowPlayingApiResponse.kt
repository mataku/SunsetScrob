package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
class NowPlayingApiResponse(
    @Json(name = "nowplaying")
    val nowPlaying: NowPlaying
) {

    @JsonSerializable
    data class NowPlaying(
        val artist: NowPlayingArtist,
        val ignoredMessage: IgnoredMessage,
        val album: NowPlayingAlbum,
        //        val albumArtist: NowPlayingArtist,
        val track: NowPlayingTrack
    )
}