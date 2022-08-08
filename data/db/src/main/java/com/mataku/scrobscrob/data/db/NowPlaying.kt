package com.mataku.scrobscrob.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NowPlayingTrackEntity(
  @PrimaryKey
  var id: Long = 0,

  @ColumnInfo(name = "artist_name")
  var artistName: String = "",

  @ColumnInfo(name = "track_name")
  var trackName: String = "",

  @ColumnInfo(name = "album_name")
  var albumName: String = "",

  @ColumnInfo(name = "artwork")
  var artwork: String = "",

  @ColumnInfo(name = "duration")
  var duration: Long = 0L,

  @ColumnInfo(name = "timestamp")
  val timeStamp: Long = System.currentTimeMillis() / 1000L
)
