package com.mataku.scrobscrob.app.model.entity

import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertNotNull

class TopAlbumsApiResponseTest {
    @Test
    @Throws
    fun testParsingJson() {
        val gson = Gson()
        val response = gson.fromJson(
                TestUtils.getAssetFileString("top_albums.json"),
                TopAlbumsApiResponse::class.java
        )
        assertNotNull(response.topAlbums)
        val albums = response.topAlbums.albums
        assertNotNull(albums)
        val album = albums[0]
        assertNotNull(album.name)
        assertNotNull(album.artist)
        assertNotNull(album.url)
        assertNotNull(album.playCount)
        assertNotNull(album.imageList)
    }
}