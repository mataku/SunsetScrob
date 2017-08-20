package com.mataku.scrobscrob.app.model.entity

data class Scrobble(
        val artist: NowPlayingArtist,
        val ignoredMessage: IgnoredMessage,
        val albumArtist: NowPlayingArtist,
        val timeStamp: String,
        val album: NowPlayingAlbum,
        val track: NowPlayingTrack
)