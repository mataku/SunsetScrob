package com.mataku.scrobscrob.app.model

import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.core.api.endpoint.AlbumArtist
import com.mataku.scrobscrob.core.api.endpoint.Image

object DummyAlbums {
    val contents = listOf(
        Album(
            name = "生まれてから初めて見た夢",
            artist = AlbumArtist(
                name = "乃木坂46"
            ),
            imageList = listOf(
                Image(
                    imageUrl = "https://lastfm.freetls.fastly.net/i/u/300x300/115974ea870713fc196e5597626098b6.jpg"
                )
            ),
            url = "https://last.fm",
            playcount = "10"
        ),
        Album(
            name = "生まれてから初めて見た夢",
            artist = AlbumArtist(
                name = "乃木坂46"
            ),
            imageList = listOf(
                Image(
                    imageUrl = "https://lastfm.freetls.fastly.net/i/u/300x300/115974ea870713fc196e5597626098b6.jpg"
                )
            ),
            url = "https://www.last.fm/music/PassCode",
            playcount = "9"
        ),
        Album(
            name = "生まれてから初めて見た夢",
            artist = AlbumArtist(
                name = "乃木坂46"
            ),
            imageList = listOf(
                Image(
                    imageUrl = "https://lastfm.freetls.fastly.net/i/u/300x300/115974ea870713fc196e5597626098b6.jpg"
                )
            ),
            url = "https://last.fm",
            playcount = "8"
        )
    )

}