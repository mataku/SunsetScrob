package com.mataku.scrobscrob.auth.webauth

sealed interface LastFmWebAuthResult {
  data class Success(val token: String) : LastFmWebAuthResult
  data object Canceled : LastFmWebAuthResult
  data object Failed : LastFmWebAuthResult
}
