package com.mataku.scrobscrob.core.api.endpoint

data class TopArtistsEndpoint(
    override val path: String,
    override val requestType: RequestType = RequestType.GET
) : Endpoint