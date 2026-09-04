package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable

@Immutable
data class NowPlaying(
  val artistName: String,
  val trackName: String,
  val albumName: String
)

@Immutable
data class NowPlayingTrack(
  val artistName: String,
  val trackName: String,
  val albumName: String,
  val artwork: String,
  val duration: Long
)

@Immutable
data class NowPlayingTrackEntity(
  val artistName: String = "",

  val trackName: String = "",

  val albumName: String = "",

  val artwork: String = "",

  val duration: Long = 0L,

  val timeStamp: Long = System.currentTimeMillis()
) {
  fun overScrobblePoint(): Boolean {
    val now = System.currentTimeMillis()
    return (now - timeStamp) > (duration / 2)
  }
}
