package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.core.entity.AlbumInfoApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test
import kotlin.test.assertNotNull

class AlbumInfoTest {
    @Test
    @Throws
    fun testParsingJson() {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter<AlbumInfoApiResponse>(AlbumInfoApiResponse::class.java)
        val response = adapter.fromJson(
            TestUtils.getAssetFileString("album_get_info.json")
        )
        assertNotNull(response?.albumInfo)
    }
}