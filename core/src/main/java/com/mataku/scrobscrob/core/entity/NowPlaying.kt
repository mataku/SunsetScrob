package com.mataku.scrobscrob.core.entity

data class NowPlaying(
  val artistName: String,
  val trackName: String,
  val albumName: String
)

data class NowPlayingTrack(
  val artistName: String,
  val trackName: String,
  val albumName: String,
  val artwork: String,
  val duration: Long
)

data class NowPlayingTrackEntity(
  var artistName: String = "",

  var trackName: String = "",

  var albumName: String = "",

  var artwork: String = "",

  var duration: Long = 0L,

  val timeStamp: Long = System.currentTimeMillis()
) {
  fun overScrobblePoint(): Boolean {
    val now = System.currentTimeMillis()
    return (now - timeStamp) > (duration / 2)
  }
}
