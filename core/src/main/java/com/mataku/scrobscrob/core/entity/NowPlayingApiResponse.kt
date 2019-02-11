package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

class NowPlayingApiResponse(
    @Json(name = "nowplaying")
    val nowPlaying: NowPlaying
) {

    data class NowPlaying(
        val artist: NowPlayingArtist,
        val ignoredMessage: IgnoredMessage,
        val album: NowPlayingAlbum,
        //        val albumArtist: NowPlayingArtist,
        val track: NowPlayingTrack
    )
}