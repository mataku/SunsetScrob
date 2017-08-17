package com.mataku.scrobscrob.app.model.entity

data class NowPlaying(
        val artist: Artist,
        val ignoredMessage: IgnoredMessage,
        val album: Album,
        val albumArtist: AlbumArtist,
        val track: Track
)