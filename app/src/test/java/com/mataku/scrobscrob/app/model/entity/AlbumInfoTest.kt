package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.app.model.api.ApplicationJsonAdapterFactory
import com.squareup.moshi.Moshi
import org.junit.Test
import kotlin.test.assertNotNull

class AlbumInfoTest {
    @Test
    @Throws
    fun testParsingJson() {
        val moshi = Moshi.Builder().add(ApplicationJsonAdapterFactory.INSTANCE).build()
        val adapter = moshi.adapter<AlbumInfoApiResponse>(AlbumInfoApiResponse::class.java)
        val response = adapter.fromJson(
                TestUtils.getAssetFileString("album_get_info.json")
        )
        assertNotNull(response?.albumInfo)
    }
}