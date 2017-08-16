package com.mataku.scrobscrob.app.model.entity

import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertNotNull

class MobileSessionTest {
    @Test
    @Throws(Exception::class)
    fun testParsingJson() {
        val gson = Gson()
        val response = gson.fromJson(
                TestUtils.getAssetFileString("mobile_session.json"),
                MobileSessionApiResponse::class.java
        )
        assertNotNull(response.mobileSession)
        val mobileSession = response.mobileSession
        assertNotNull(mobileSession.subscriber)
        assertNotNull(mobileSession.name)
        assertNotNull(mobileSession.key)
    }
}