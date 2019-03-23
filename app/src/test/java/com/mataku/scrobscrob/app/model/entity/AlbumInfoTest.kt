package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.core.entity.AlbumInfoApiResponse
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
class AlbumInfoTest : Spek({

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    describe("AlbumInfo") {
        beforeGroup {
            Thread.currentThread().setUncaughtExceptionHandler { _, _ -> fail() }
        }
        afterGroup {}
        context("successful request") {
            it("Parse correctly") {
                val adapter = moshi.adapter<AlbumInfoApiResponse>(AlbumInfoApiResponse::class.java)
                val response = adapter.fromJson(
                    TestUtils.getAssetFileString("album_get_info.json")
                )
                assertNotNull(response?.albumInfo)
            }
        }
    }
})