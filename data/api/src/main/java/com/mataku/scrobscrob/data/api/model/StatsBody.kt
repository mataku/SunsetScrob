package com.mataku.scrobscrob.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatsBody(
  @SerialName("listeners")
  val listeners: String,

  @SerialName("plays")
  val playCount: String
)
