package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.TrackInfoBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TrackInfoEndpoint(
  override val path: String = "/2.0/?method=track.getInfo&format=json",
  override val requestType: HttpMethod = HttpMethod.GET,
  override val params: Map<String, Any?>
) : Endpoint<TrackInfoApiResponse>

@Serializable
data class TrackInfoApiResponse(
  @SerialName("track")
  val trackInfo: TrackInfoBody
)
