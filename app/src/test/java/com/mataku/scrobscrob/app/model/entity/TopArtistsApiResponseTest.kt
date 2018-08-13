package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.app.model.api.ApplicationJsonAdapterFactory
import com.squareup.moshi.Moshi
import org.junit.Test
import kotlin.test.assertNotNull

class TopArtistsApiResponseTest {
    private val moshi = Moshi.Builder().add(ApplicationJsonAdapterFactory.INSTANCE).build()

    @Test
    @Throws
    fun testParsingJson() {
        val jsonAdapter = moshi.adapter<TopArtistsApiResponse>(TopArtistsApiResponse::class.java)
        val response = jsonAdapter.fromJson(
                TestUtils.getAssetFileString("top_artists.json")
        )
        assertNotNull(response?.topArtists)
        val artists = response?.topArtists?.artists
        assertNotNull(artists)
        val artist = artists?.get(0)
        assertNotNull(artist?.name)
        assertNotNull(artist?.url)
        assertNotNull(artist?.image)
    }
}