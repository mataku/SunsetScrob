package com.mataku.scrobscrob.ui_common.extension

// e.g. 214 (seconds) -> 3:34
fun String?.toReadableString(): String? {
  this ?: return null
  return runCatching {
    this.toIntOrNull()?.let {
      val minutes = it / 60
      val seconds = it % 60
      "${minutes}:${seconds.toString().padStart(2, '0')}"
    }
  }.getOrNull()
}
