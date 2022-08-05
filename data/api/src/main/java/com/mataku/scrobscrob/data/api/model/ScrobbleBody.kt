package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScrobbleApiResponse(
  @SerialName("scrobbles")
  val scrobbleResult: ScrobbleResultBody? = null
)

@Serializable
data class ScrobbleResultBody(
  @SerialName("@attr")
  val attr: ScrobbleAttrBody,

  @SerialName("scrobble")
  val scrobble: ScrobbleBody
)

@Serializable
data class ScrobbleAttrBody(
  @SerialName("accepted")
  val accepted: Int?,
  @SerialName("ignored")
  val ignored: Int?
)

@Serializable
data class ScrobbleBody(
  @SerialName("artist")
  val artist: ScrobbleValueResult,

  @SerialName("ignoredMessage")
  val ignoredMessage: IgnoredMessage,

  @SerialName("timestamp")
  val timestamp: String,

  @SerialName("album")
  val album: ScrobbleValueResult,

  @SerialName("track")
  val track: ScrobbleValueResult
)

@Serializable
data class ScrobbleValueResult(
  @SerialName("corrected")
  val corrected: String,

  @SerialName("#text")
  val name: String
)

@Serializable
data class IgnoredMessage(
  @SerialName("code")
  val code: String,

  @SerialName("#text")
  val message: String
)
