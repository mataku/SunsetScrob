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
