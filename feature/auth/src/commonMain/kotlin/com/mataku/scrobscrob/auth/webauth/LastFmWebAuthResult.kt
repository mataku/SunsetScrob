package com.mataku.scrobscrob.auth.webauth

sealed interface LastFmWebAuthResult {
  data class Success(val token: String) : LastFmWebAuthResult
  data object Closed : LastFmWebAuthResult
  data object Failed : LastFmWebAuthResult
}
