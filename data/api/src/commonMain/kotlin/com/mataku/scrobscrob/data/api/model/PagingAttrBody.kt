package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagingAttrBody(
  @SerialName("page")
  val page: String = "",

  @SerialName("perPage")
  val perPage: String = "",

  @SerialName("totalPages")
  val totalPages: String = "",

  @SerialName("total")
  val total: String = ""
)
