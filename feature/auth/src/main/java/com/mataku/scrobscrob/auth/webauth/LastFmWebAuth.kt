package com.mataku.scrobscrob.auth.webauth

import java.net.URI

object LastFmWebAuth {
  const val CALLBACK_HOST = "sunsetscrob.mataku.com"
  const val CALLBACK_PATH = "/auth/lastfm"
  const val TOKEN_PARAM = "token"

  fun tokenFromCallback(url: String): String? {
    val uri = runCatching { URI(url) }.getOrNull() ?: return null
    if (uri.host != CALLBACK_HOST || uri.path != CALLBACK_PATH) return null
    val query = uri.rawQuery ?: return null
    return query.split('&')
      .map { it.substringBefore('=') to it.substringAfter('=', "") }
      .firstOrNull { (key, _) -> key == TOKEN_PARAM }
      ?.second
      ?.takeIf { it.isNotBlank() }
  }
}
