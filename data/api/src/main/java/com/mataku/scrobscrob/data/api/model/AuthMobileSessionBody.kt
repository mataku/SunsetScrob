package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthMobileSessionApiResponse(
  @SerialName("session")
  val mobileSession: MobileSessionBody
)

@Serializable
data class MobileSessionBody(
  @SerialName("name")
  val name: String,
  @SerialName("key")
  val key: String
)
