package com.mataku.scrobscrob.core.entity

import java.util.Date

data class Wiki(
  val published: Date,
  val summary: String?,
  val content: String?
)
