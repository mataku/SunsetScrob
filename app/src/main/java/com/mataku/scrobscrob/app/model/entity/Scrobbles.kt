package com.mataku.scrobscrob.app.model.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
class Scrobbles(
    @Json(name = "@attr")
    val attr: ScrobbleAttr,
    @Json(name = "scrobble")
    val scrobble: Scrobble
) {

    data class Scrobble(
        val artist: NowPlayingArtist,
        val ignoredMessage: IgnoredMessage,
        val albumArtist: NowPlayingArtist,
        val timeStamp: String,
        val album: NowPlayingAlbum,
        val track: NowPlayingTrack
    )

    data class ScrobbleAttr(
        val accepted: Int,
        val ignored: Int
    )
}