package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class UserInfo(
  val name: String,
  val playCount: String,
  val artistCount: String,
  val trackCount: String,
  val albumCount: String,
  val imageList: ImmutableList<Image>,
  val url: String
)
