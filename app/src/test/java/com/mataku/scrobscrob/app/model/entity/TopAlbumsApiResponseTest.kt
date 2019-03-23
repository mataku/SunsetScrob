package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.core.entity.TopAlbumsApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertNotNull
import kotlin.test.fail

@RunWith(JUnitPlatform::class)
class TopAlbumsApiResponseTest : Spek({

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    describe("TopAlbumsApiResponse") {
        beforeGroup {
            Thread.currentThread().setUncaughtExceptionHandler { _, _ -> fail() }
        }
        afterGroup {}

        it("Parse correctly") {
            val jsonAdapter = moshi.adapter<TopAlbumsApiResponse>(TopAlbumsApiResponse::class.java)
            val response = jsonAdapter.fromJson(
                TestUtils.getAssetFileString("top_albums.json")
            )
            assertNotNull(response?.topAlbums)
            val albums = response?.topAlbums?.albums
            assertNotNull(albums)
            val album = albums?.get(0)
            assertNotNull(album?.name)
            assertNotNull(album?.artist)
            assertNotNull(album?.url)
            assertNotNull(album?.playCount)
            assertNotNull(album?.imageList)
        }
    }
})