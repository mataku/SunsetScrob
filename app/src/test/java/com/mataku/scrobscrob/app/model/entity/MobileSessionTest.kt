package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.app.model.api.ApplicationJsonAdapterFactory
import com.mataku.scrobscrob.core.entity.MobileSessionApiResponse
import com.squareup.moshi.Moshi
import org.junit.Test
import kotlin.test.assertNotNull

class MobileSessionTest {

    private val moshi = Moshi.Builder().add(ApplicationJsonAdapterFactory.INSTANCE).build()

    @Test
    @Throws(Exception::class)
    fun testParsingJson() {
        val jsonAdapter = moshi.adapter<MobileSessionApiResponse>(MobileSessionApiResponse::class.java)
        val response = jsonAdapter.fromJson(
            TestUtils.getAssetFileString("mobile_session.json")
        )
        assertNotNull(response?.mobileSession)
        val mobileSession = response?.mobileSession
        assertNotNull(mobileSession?.subscriber)
        assertNotNull(mobileSession?.name)
        assertNotNull(mobileSession?.key)
    }
}