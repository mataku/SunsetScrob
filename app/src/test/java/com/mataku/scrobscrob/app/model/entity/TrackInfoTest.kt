package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.core.entity.TrackInfoApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test
import kotlin.test.assertNotNull

class TrackInfoTest {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Test
    @Throws
    fun testParsingJson() {
        val jsonAdapter = moshi.adapter<TrackInfoApiResponse>(TrackInfoApiResponse::class.java)
        val response = jsonAdapter.fromJson(
            TestUtils.getAssetFileString("track_get_info.json")
        )
        assertNotNull(response?.trackInfo)
        val trackInfo = response?.trackInfo
        assertNotNull(trackInfo?.duration)
        val album = trackInfo?.album
        assertNotNull(album)
        assertNotNull(album?.imageList)
        val smallImage = album?.imageList?.get(0)
        assertNotNull(smallImage?.imageUrl)
        assertNotNull(smallImage?.size)
    }
}