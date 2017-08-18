package com.mataku.scrobscrob.app.model.entity

data class Scrobble(
        val artist: Artist,
        val ignoredMessage: IgnoredMessage,
        val albumArtist: AlbumArtist,
        val timeStamp: String,
        val album: Album,
        val track: Track
)