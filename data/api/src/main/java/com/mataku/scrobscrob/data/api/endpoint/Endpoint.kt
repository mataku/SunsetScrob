package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.BuildConfig
import io.ktor.http.HttpMethod
import java.security.MessageDigest

interface Endpoint<out T> {
  val path: String
  val params: Map<String, Any?>
    get() = mapOf()
  val requestType: HttpMethod
}

fun Endpoint<*>.generateApiSignature(): String {
  var str = ""
  val paramsCopy = params.toMutableMap()
  paramsCopy["api_key"] = BuildConfig.API_KEY
  paramsCopy.toSortedMap().forEach { (k, v) ->
    str += k + v
  }
  str += BuildConfig.SHARED_SECRET

  val messageDigest = MessageDigest.getInstance("MD5")
  val byteData = str.toByteArray()
  messageDigest.update(byteData)
  val digest = messageDigest.digest()
  val stringBuilder = StringBuilder()
  digest.indices.forEach { i ->
    val b = (0xFF and digest[i].toInt())
    if (b < 16)
      stringBuilder.append("0")
    stringBuilder.append(Integer.toHexString(b))
  }
  return stringBuilder.toString()
}
