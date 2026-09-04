package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoApiResponse(
  @SerialName("user")
  val userInfo: UserInfoBody
)

@Serializable
data class UserInfoBody(
  val name: String,

  @SerialName("playcount")
  val playCount: String,

  @SerialName("artist_count")
  val artistCount: String,

  @SerialName("track_count")
  val trackCount: String,

  @SerialName("album_count")
  val albumCount: String,

  @SerialName("image")
  val imageList: List<ImageBody>?,

  val url: String
)
