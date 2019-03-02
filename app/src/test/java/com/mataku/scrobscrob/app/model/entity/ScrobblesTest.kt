package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.core.entity.ScrobblesApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test
import kotlin.test.assertNotNull

class ScrobblesTest {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Test
    @Throws
    fun testParsingJson() {
        val jsonAdapter = moshi.adapter<ScrobblesApiResponse>(ScrobblesApiResponse::class.java)
        val response = jsonAdapter.fromJson(
            TestUtils.getAssetFileString("scrobbles.json")
        )
        assertNotNull(response?.scrobbles)
        val scrobbles = response?.scrobbles
        assertNotNull(scrobbles?.attr)
        assertNotNull(scrobbles?.scrobble)
    }
}