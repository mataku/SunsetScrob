package com.mataku.scrobscrob.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
  val name: String,
  val url: String
)
