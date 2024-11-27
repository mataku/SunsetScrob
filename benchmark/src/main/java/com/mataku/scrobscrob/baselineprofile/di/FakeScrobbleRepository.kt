package com.mataku.scrobscrob.baselineprofile.di

import com.mataku.scrobscrob.core.entity.NowPlayingTrackEntity
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.ScrobbleResult
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeScrobbleRepository : ScrobbleRepository {
  override suspend fun recentTracks(page: Int): Flow<List<RecentTrack>> = flowOf(
    listOf(
      RecentTrack(
        artistName = "SooooooooooooooooooLoooooooooooongArtistName",
        images = persistentListOf(),
        albumName = "SoooooooooooooooooooLoooooooooooongAlbumName",
        name = "track name",
        url = "",
        date = null
      )
    )
  )

  override suspend fun scrobble(currentTrack: NowPlayingTrackEntity): Flow<ScrobbleResult> = flowOf(ScrobbleResult(accepted = false))
}
