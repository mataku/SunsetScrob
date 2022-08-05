package com.mataku.scrobscrob.app.model

import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.Image

object DummyAlbums {
  val contents = listOf(
    AlbumInfo(
      artist = "乃木坂46",
      title = "生まれてから初めて見た夢",
      imageList = listOf(
        Image(
          size = "extralarge",
          url = "https://lastfm.freetls.fastly.net/i/u/300x300/115974ea870713fc196e5597626098b6.jpg"
        )
      ),
      playCount = "100",
      url = "https://example.com"
    ),
  )
}
