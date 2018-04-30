package com.mataku.scrobscrob.app.model.entity

import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertNotNull

class TopArtistsApiResponseTest {
    @Test
    @Throws
    fun testParsingJson() {
        val gson = Gson()
        val response = gson.fromJson(
                TestUtils.getAssetFileString("top_artists.json"),
                TopArtistsApiResponse::class.java
        )
        assertNotNull(response.topArtists)
        val artists = response.topArtists.artists
        assertNotNull(artists)
        val artist = artists[0]
        assertNotNull(artist.name)
        assertNotNull(artist.url)
        assertNotNull(artist.image)
    }
}