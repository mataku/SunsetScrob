package com.mataku.scrobscrob.data.api.endpoint

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LastFmErrorResponse(
  @SerialName("message")
  val message: String
) {
  companion object {
    const val ERROR_CODE_INVALID_SESSION_KEY = 9
  }
}

data class LastFmApiError(
  val errorMessage: String
) : Throwable(errorMessage)
