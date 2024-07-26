package com.mataku.scrobscrob.home

enum class HomeTabType(val tabName: String) {
  SCROBBLE("Scrobble"),
  ALBUM("Album"),
  ARTIST("Artist");

  companion object {
    fun findByIndex(index: Int): HomeTabType = entries.getOrNull(index) ?: SCROBBLE
  }
}
