package com.mataku.scrobscrob.core.entity

data class LovedTrack(
  val artist: String,
  val images: List<Image>,
  val name: String,
  val url: String,
  val date: String? = null
)
