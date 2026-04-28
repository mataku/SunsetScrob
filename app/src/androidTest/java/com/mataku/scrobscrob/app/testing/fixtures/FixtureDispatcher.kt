package com.mataku.scrobscrob.app.testing.fixtures

import android.content.res.AssetManager
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

class FixtureDispatcher(private val assets: AssetManager) {

  fun MockRequestHandleScope.dispatch(request: HttpRequestData): HttpResponseData {
    val method = request.url.parameters["method"].orEmpty()
    val body = method.fixtureName()?.let { name ->
      runCatching { readAsset(name) }.getOrNull()
    }
    return if (body == null) {
      respondError(
        HttpStatusCode.NotImplemented,
        "No fixture for method=$method (path=${request.url.encodedPath})"
      )
    } else {
      respond(
        content = ByteReadChannel(body),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
      )
    }
  }

  private fun readAsset(path: String): String =
    assets.open(path).bufferedReader().use { it.readText() }

  private fun String.fixtureName(): String? = when (lowercase()) {
    "auth.getmobilesession" -> "mobile_session.json"
    "user.getrecenttracks" -> "recent_tracks.json"
    "user.getinfo" -> "user_get_info.json"
    "user.gettopalbums" -> "top_albums.json"
    "user.gettopartists" -> "top_artists.json"
    "user.getlovedtracks" -> "user_loved_tracks.json"
    "chart.gettoptracks" -> "chart_top_tracks.json"
    "chart.gettopartists" -> "chart_top_artists.json"
    "track.getinfo" -> "track_get_info.json"
    "album.getinfo" -> "album_get_info.json"
    else -> null
  }
}
