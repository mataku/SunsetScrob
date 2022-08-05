package com.mataku.scrobscrob.core.entity

data class RecentTrack(
  val artistName: String,
  val images: List<Image>,
  val albumName: String,
  val name: String,
  val url: String,
  val date: String? = null
)
