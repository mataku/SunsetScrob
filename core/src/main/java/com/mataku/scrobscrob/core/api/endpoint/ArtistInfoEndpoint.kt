package com.mataku.scrobscrob.core.api.endpoint

import com.mataku.scrobscrob.core.entity.TopTags
import io.ktor.http.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ArtistInfoEndpoint(
  override val path: String = "/2.0/?method=artist.getInfo&format=json",
  override val requestType: HttpMethod = HttpMethod.Get,
  override val params: Map<String, Any?>
) : Endpoint

@Serializable
data class ArtistInfoApiResponse(
  @SerialName("artist")
  val artistInfo: ArtistInfo
)

@Serializable
data class ArtistInfo(
  @SerialName("name")
  val name: String,
  @SerialName("image")
  val imageList: List<Image>,

  @SerialName("toptags")
  val topTags: TopTags
)
