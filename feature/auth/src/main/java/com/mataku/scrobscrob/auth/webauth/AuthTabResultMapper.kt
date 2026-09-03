package com.mataku.scrobscrob.auth.webauth

import androidx.browser.auth.AuthTabIntent
import timber.log.Timber

internal fun mapAuthTabResult(resultCode: Int, resultUri: String?): LastFmWebAuthResult {
  if (resultCode == AuthTabIntent.RESULT_CANCELED) return LastFmWebAuthResult.Canceled
  if (resultCode != AuthTabIntent.RESULT_OK) {
    Timber.w("Auth Tab finished with resultCode=%d", resultCode)
    return LastFmWebAuthResult.Failed
  }
  val token = resultUri?.let(LastFmWebAuth::tokenFromCallback)
  return if (token == null) LastFmWebAuthResult.Failed else LastFmWebAuthResult.Success(token)
}
