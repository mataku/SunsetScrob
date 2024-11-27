package com.mataku.scrobscrob.baselineprofile.di

import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.core.entity.TopArtistInfo
import com.mataku.scrobscrob.data.repository.TopArtistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTopArtistsRepository : TopArtistsRepository {
  override suspend fun fetchTopArtists(
    page: Int,
    username: String,
    timeRangeFiltering: TimeRangeFiltering
  ): Flow<List<TopArtistInfo>> = flowOf(emptyList())
}
