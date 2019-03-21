package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.core.entity.TrackInfoApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertNotNull

@RunWith(JUnitPlatform::class)
class TrackInfoTest : Spek({
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    describe("TrackInfo") {
        context("successful request") {
            it("Parse correctly") {
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
    }
})