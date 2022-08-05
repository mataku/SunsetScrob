package com.mataku.scrobscrob.core.entity

data class AlbumInfo(
  val artist: String,
  val title: String,
  val imageList: List<Image>,
  val playCount: String,
  val url: String
)

data class TrackAlbumInfo(
  val artist: String,
  val title: String,
  val imageList: List<Image>
  // playCount is missing in track.getInfo
  // https://www.last.fm/api/show/track.getInfo
)
