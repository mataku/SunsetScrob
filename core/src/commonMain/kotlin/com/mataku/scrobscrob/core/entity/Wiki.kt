package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable

@Immutable
data class Wiki(
  val published: String?,
  val summary: String?,
  val content: String?
)
