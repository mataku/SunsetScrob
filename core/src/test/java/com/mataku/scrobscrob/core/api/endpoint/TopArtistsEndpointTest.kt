package com.mataku.scrobscrob.core.api.endpoint

import com.mataku.scrobscrob.core.JsonTestHelper
import kotlinx.serialization.json.Json
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@RunWith(JUnitPlatform::class)
class TopArtistsEndpointTest : Spek({
    describe("TopArtistsApiResponse") {
        context("200") {
            it("Parse correctly") {
                val data = Json.decodeFromString(
                    TopArtistsApiResponse.serializer(),
                    JsonTestHelper.getAssetFileString("top_artists.json")
                )
                assertNotNull(data)
                assertNotNull(data.topArtists)
                assertFalse(data.topArtists.artists.isNullOrEmpty())
            }
        }
    }
})