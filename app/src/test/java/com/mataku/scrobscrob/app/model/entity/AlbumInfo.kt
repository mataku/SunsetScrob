package com.mataku.scrobscrob.app.model.entity

import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertNotNull

class AlbumInfo {
    @Test
    @Throws
    fun testParsingJson() {
        val gson = Gson()
        val response = gson.fromJson(
                TestUtils.getAssetFileString("album_get_info.json"),
                AlbumInfoApiResponse::class.java
        )
        assertNotNull(response.albumInfo)
    }
}