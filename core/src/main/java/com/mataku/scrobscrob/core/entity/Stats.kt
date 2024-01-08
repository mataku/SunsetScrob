package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable

@Immutable
data class Stats(
  val listeners: String = "",
  val playCount: String = ""
)
