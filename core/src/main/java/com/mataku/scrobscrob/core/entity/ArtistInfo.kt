package com.mataku.scrobscrob.core.entity

data class ArtistInfo(
  val name: String,
  val imageList: List<Image>,
  val topTags: List<Tag>,
  val playCount: String,
  val url: String
)
