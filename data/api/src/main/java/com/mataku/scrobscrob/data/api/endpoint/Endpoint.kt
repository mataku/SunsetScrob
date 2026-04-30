package com.mataku.scrobscrob.data.api.endpoint

interface Endpoint<out T> {
  val path: String
  val params: Map<String, Any?>
    get() = mapOf()
  val requestType: HttpMethod
}
