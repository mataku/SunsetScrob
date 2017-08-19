package com.mataku.scrobscrob.app.model.entity

import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertNotNull

class ScrobblesTest {
    @Test
    @Throws
    fun testParsingJson() {
        val gson = Gson()
        val response = gson.fromJson(
                TestUtils.getAssetFileString("scrobbles.json"),
                ScrobblesApiResponse::class.java
        )
        assertNotNull(response.scrobbles)
        val scrobbles = response.scrobbles
        assertNotNull(scrobbles.attr)
        assertNotNull(scrobbles.scrobble)
    }
}