package com.mataku.scrobscrob.app.model.entity

import com.mataku.scrobscrob.core.entity.ScrobblesApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertNotNull
import kotlin.test.fail

@RunWith(JUnitPlatform::class)
class ScrobblesTest : Spek({

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    describe("Model properties") {
        beforeGroup {
            Thread.currentThread().setUncaughtExceptionHandler { _, _ -> fail() }
        }
        afterGroup {}
        context("current") {
            it("way") {
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
    }
})