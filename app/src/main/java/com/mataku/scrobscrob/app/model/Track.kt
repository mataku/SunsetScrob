package com.mataku.scrobscrob.app.model

import java.util.*

class Track(artistName: String, trackName: String, albumName: String, startedAt: Date) {
    var artistName: String = artistName
    var trackName: String = trackName
    var albumName: String = albumName
    var startedAt: Date = startedAt
}