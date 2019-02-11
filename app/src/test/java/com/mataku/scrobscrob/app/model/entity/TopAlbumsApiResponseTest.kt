package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.app.model.api.ApplicationJsonAdapterFactory
import com.mataku.scrobscrob.core.entity.TopAlbumsApiResponse
import com.squareup.moshi.Moshi
import org.junit.Test
import kotlin.test.assertNotNull

class TopAlbumsApiResponseTest {

    private val moshi = Moshi.Builder().add(ApplicationJsonAdapterFactory.INSTANCE).build()

    @Test
    @Throws
    fun testParsingJson() {
        val jsonAdapter = moshi.adapter<TopAlbumsApiResponse>(TopAlbumsApiResponse::class.java)
        val response = jsonAdapter.fromJson(
            TestUtils.getAssetFileString("top_albums.json")
        )
        assertNotNull(response?.topAlbums)
        val albums = response?.topAlbums?.albums
        assertNotNull(albums)
        val album = albums?.get(0)
        assertNotNull(album?.name)
        assertNotNull(album?.artist)
        assertNotNull(album?.url)
        assertNotNull(album?.playCount)
        assertNotNull(album?.imageList)
    }
}