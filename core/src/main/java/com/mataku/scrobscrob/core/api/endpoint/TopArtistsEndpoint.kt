package com.mataku.scrobscrob.core.api.endpoint

import io.ktor.http.HttpMethod

data class TopArtistsEndpoint(
    override val path: String = "/2.0/?method=user.getTopArtists&format=json",
    override val requestType: HttpMethod = HttpMethod.Get
) : Endpoint