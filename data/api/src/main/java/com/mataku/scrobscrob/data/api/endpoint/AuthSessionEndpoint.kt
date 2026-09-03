package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.model.AuthSessionApiResponse

data class AuthSessionEndpoint(
  override val path: String = "/2.0/?method=auth.getSession&format=json",
  override val requestType: HttpMethod = HttpMethod.GET,
  override val params: Map<String, String>
) : Endpoint<AuthSessionApiResponse>
