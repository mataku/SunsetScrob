package com.mataku.scrobscrob.core.api.endpoint

import io.ktor.http.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ScrobbleEndpoint(
  override val path: String = "/2.0/?method=track.scrobble&format=json",
  override val requestType: HttpMethod = HttpMethod.Post,
  override val params: Map<String, String>
) : Endpoint

@Serializable
data class ScrobbleApiResponse(
  val scrobbleResult: ScrobbleResult?
)

@Serializable
data class ScrobbleResult(
  val scrobbleAttr: ScrobbleAttr
)

@Serializable
data class ScrobbleAttr(
  @SerialName("accepted")
  val accepted: Int?,
  @SerialName("ignored")
  val ignored: Int?
)
