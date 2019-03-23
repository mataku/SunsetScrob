package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.core.entity.NowPlayingApiResponse
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
class NowPlayingTest : Spek({

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    describe("NowPlaying") {
        context("successful request") {
            it("Parse correctly") {
                val jsonAdapter = moshi.adapter<NowPlayingApiResponse>(NowPlayingApiResponse::class.java)
                val response = jsonAdapter.fromJson(
                    TestUtils.getAssetFileString("now_playing.json")
                )
                assertNotNull(response?.nowPlaying)
                val nowPlaying = response?.nowPlaying
                assertNotNull(nowPlaying?.album)
                assertNotNull(nowPlaying?.artist)
                assertNotNull(nowPlaying?.ignoredMessage)
                assertNotNull(nowPlaying?.track)
            }
        }
    }
})