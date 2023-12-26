package com.mataku.scrobscrob.data.db

data class NowPlayingTrackEntity(
  var artistName: String = "",

  var trackName: String = "",

  var albumName: String = "",

  var artwork: String = "",

  var duration: Long = 0L,

  val timeStamp: Long = System.currentTimeMillis()
)

fun NowPlayingTrackEntity.overScrobblePoint(): Boolean {
  val now = System.currentTimeMillis()
  return (now - timeStamp) > (duration / 2)
}
