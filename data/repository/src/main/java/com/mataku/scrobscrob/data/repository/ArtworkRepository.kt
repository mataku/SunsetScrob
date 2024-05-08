package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.api.LastFmService
import javax.inject.Inject
import javax.inject.Singleton

interface ArtworkRepository {
}

@Singleton
class ArtworkRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
) : ArtworkRepository {
}
