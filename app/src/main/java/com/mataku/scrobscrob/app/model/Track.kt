package com.mataku.scrobscrob.app.model

data class Track(
        var artistName: String,
        var name: String,
        var albumName: String,
        var playingTime: Long,
        var timeStamp: Long
)