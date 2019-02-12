package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

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