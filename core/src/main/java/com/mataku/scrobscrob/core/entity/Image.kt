package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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

@OptIn(ExperimentalContracts::class)
fun String?.isInvalidArtwork(): Boolean {
  contract {
    returns(false) implies (this@isInvalidArtwork != null)
  }

  if (this.isNullOrEmpty()) return true

  val lastElement = this.split("/").lastOrNull()?.split(".")?.get(0) ?: return true
  return lastElement == "2a96cbd8b46e442fc41c2b86b821562f"
}

