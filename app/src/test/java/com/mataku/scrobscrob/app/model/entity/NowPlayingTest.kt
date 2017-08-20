package com.mataku.scrobscrob.app.model.entity

import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertNotNull

class NowPlayingTest {
    @Test
    @Throws
    fun testPArsingJson() {
        val gson = Gson()
        val response = gson.fromJson(
                TestUtils.getAssetFileString("now_playing.json"),
                NowPlayingApiResponse::class.java
        )
        assertNotNull(response.nowPlaying)
        val nowPlaying = response.nowPlaying
        assertNotNull(nowPlaying.album)
        assertNotNull(nowPlaying.artist)
        assertNotNull(nowPlaying.ignoredMessage)
        assertNotNull(nowPlaying.track)
    }
}