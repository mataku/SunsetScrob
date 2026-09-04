package com.mataku.scrobscrob.test_helper.integration.fixture

import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.AlbumInfoTrack
import com.mataku.scrobscrob.core.entity.Tag
import kotlinx.collections.immutable.persistentListOf

val sampleAlbumInfo: AlbumInfo = AlbumInfo(
  albumName = "Drama",
  artistName = "Drama",
  images = persistentListOf(),
  tags = persistentListOf(
    Tag("K-POP", ""),
    Tag("K-POP", ""),
    Tag("K-POP", ""),
    Tag("K-POP", ""),
  ),
  url = "",
  listeners = "1000000",
  playCount = "10000000",
  tracks = persistentListOf(
    AlbumInfoTrack(duration = "100", name = "Drama", url = ""),
    AlbumInfoTrack(duration = "110", name = "Drama", url = ""),
    AlbumInfoTrack(duration = null, name = "Drama", url = ""),
  )
)
