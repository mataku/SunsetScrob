package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

class NowPlayingApiResponse(@SerializedName("nowplaying")
                            val nowPlaying: NowPlaying) {

    data class NowPlaying(
            val artist: NowPlayingArtist,
            val ignoredMessage: IgnoredMessage,
            val album: NowPlayingAlbum,
            //        val albumArtist: NowPlayingArtist,
            val track: NowPlayingTrack
    )
}