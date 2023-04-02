package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable

@Immutable
data class Image(
  val size: String,
  val url: String
)

fun List<Image>.imageUrl(): String? {
  val extraLargeImage = this.find {
    it.size == "extralarge" && it.url.isNotBlank()
  }
  if (extraLargeImage != null) {
    return extraLargeImage.url
  }
  val largeImage = this.find {
    it.size == "large" && it.url.isNotBlank()
  }
  if (largeImage != null) {
    return largeImage.url
  }

  return null

}
