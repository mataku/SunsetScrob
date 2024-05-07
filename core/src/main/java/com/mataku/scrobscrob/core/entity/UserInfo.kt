package com.mataku.scrobscrob.core.entity

data class UserInfo(
  val name: String,
  val playCount: String,
  val artistCount: String,
  val trackCount: String,
  val albumCount: String,
  val imageList: List<Image>,
  val url: String
)
