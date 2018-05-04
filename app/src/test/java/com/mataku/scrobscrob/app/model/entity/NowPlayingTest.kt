package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.app.model.api.ApplicationJsonAdapterFactory
import com.squareup.moshi.Moshi
import org.junit.Test
import kotlin.test.assertNotNull

class NowPlayingTest {

    private val moshi = Moshi.Builder().add(ApplicationJsonAdapterFactory.INSTANCE).build()

    @Test
    @Throws
    fun testParsingJson() {
        val jsonAdapter = moshi.adapter<NowPlayingApiResponse>(NowPlayingApiResponse::class.java)
        val response = jsonAdapter.fromJson(
                TestUtils.getAssetFileString("now_playing.json")
        )
        assertNotNull(response?.nowPlaying)
        val nowPlaying = response?.nowPlaying
        assertNotNull(nowPlaying?.album)
        assertNotNull(nowPlaying?.artist)
        assertNotNull(nowPlaying?.ignoredMessage)
        assertNotNull(nowPlaying?.track)
    }
}