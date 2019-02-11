package com.mataku.scrobscrob.app.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Scrobble(
    @PrimaryKey
    var id: Long = 0,

    @ColumnInfo(name = "artist_name")
    var artistName: String = "",

    @ColumnInfo(name = "track_name")
    var trackName: String = "",

    @ColumnInfo(name = "album_name")
    var albumName: String = "",

    @ColumnInfo(name = "artwork")
    var artwork: String = ""
)