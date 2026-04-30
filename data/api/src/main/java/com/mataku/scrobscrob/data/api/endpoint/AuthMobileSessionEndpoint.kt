package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.AuthMobileSessionApiResponse

data class AuthMobileSessionEndpoint(
  override val path: String = "/2.0/?method=auth.getMobileSession&format=json",
  override val requestType: HttpMethod = HttpMethod.POST,
  override val params: Map<String, String>
) : Endpoint<AuthMobileSessionApiResponse>
