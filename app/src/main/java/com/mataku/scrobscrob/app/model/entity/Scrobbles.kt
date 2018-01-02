package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

class Scrobbles(@SerializedName("@attr")
                val attr: ScrobbleAttr,
                @SerializedName("scrobble")
                val scrobble: Scrobble) {

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