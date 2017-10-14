package com.mataku.scrobscrob.app.model.entity

import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertNotNull

class TrackInfoTest {
    @Test
    @Throws
    fun testParsingJson() {
        val gson = Gson()
        val response = gson.fromJson(
                TestUtils.getAssetFileString("track_get_info.json"),
                TrackInfoApiResponse::class.java
        )
        assertNotNull(response.trackInfo)
        val trackInfo = response.trackInfo
        assertNotNull(trackInfo.duration)
        val album = trackInfo.album
        assertNotNull(album)
        assertNotNull(album.imageList)
        val smallImage = album.imageList[0]
        assertNotNull(smallImage.imageUrl)
        assertNotNull(smallImage.size)
    }
}