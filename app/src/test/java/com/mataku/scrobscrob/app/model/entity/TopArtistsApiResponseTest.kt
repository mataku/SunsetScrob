package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.core.entity.TopArtistsApiResponse
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
class TopArtistsApiResponseTest : Spek({

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    describe("TopArtistsApiResponse") {
        context("successful request") {
            it("Parse correctly") {
                val jsonAdapter = moshi.adapter<TopArtistsApiResponse>(TopArtistsApiResponse::class.java)
                val response = jsonAdapter.fromJson(
                    TestUtils.getAssetFileString("top_artists.json")
                )
                assertNotNull(response?.topArtists)
                val artists = response?.topArtists?.artists
                assertNotNull(artists)
                val artist = artists?.get(0)
                assertNotNull(artist?.name)
                assertNotNull(artist?.url)
                assertNotNull(artist?.image)
            }
        }
    }
})