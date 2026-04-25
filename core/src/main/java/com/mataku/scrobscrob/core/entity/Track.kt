package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable

@Immutable
data class Track(
  val artistName: String,
  val name: String,
  val albumName: String
)
