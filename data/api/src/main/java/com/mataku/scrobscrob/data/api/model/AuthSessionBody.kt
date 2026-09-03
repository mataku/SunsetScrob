package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthSessionApiResponse(
  @SerialName("session")
  val session: SessionBody
)

@Serializable
data class SessionBody(
  @SerialName("name")
  val name: String,
  @SerialName("key")
  val key: String
)
