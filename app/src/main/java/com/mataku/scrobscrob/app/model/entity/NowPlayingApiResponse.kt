package com.mataku.scrobscrob.app.model.entity

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