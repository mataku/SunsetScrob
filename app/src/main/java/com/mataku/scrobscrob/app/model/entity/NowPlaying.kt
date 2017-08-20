package com.mataku.scrobscrob.app.model.entity

data class NowPlaying(
        val artist: NowPlayingArtist,
        val ignoredMessage: IgnoredMessage,
        val album: NowPlayingAlbum,
        //        val albumArtist: NowPlayingArtist,
        val track: NowPlayingTrack
)