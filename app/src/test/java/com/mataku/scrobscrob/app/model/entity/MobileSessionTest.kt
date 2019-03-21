package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.core.entity.MobileSessionApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertNotNull
import kotlin.test.fail

@RunWith(JUnitPlatform::class)
class MobileSessionTest : Spek({

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    describe("MobileSession") {
        beforeGroup {
            Thread.currentThread().setUncaughtExceptionHandler { _, _ -> fail() }
        }
        afterGroup {}

        context("successful request") {
            it("Parse correctly") {
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
    }
})