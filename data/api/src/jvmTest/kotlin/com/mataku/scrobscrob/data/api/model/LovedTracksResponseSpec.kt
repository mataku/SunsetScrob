package com.mataku.scrobscrob.data.api.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class LovedTracksResponseSpec : DescribeSpec({
  // A captured user.getLovedTracks response, kept as a resource because it covers shapes a
  // hand-written fixture misses (empty mbid, percent-encoded URLs, multi-artist names).
  val rawJson = checkNotNull(
    LovedTracksResponseSpec::class.java.getResource("/user_loved_tracks.json"),
  ).readText()

  describe("parse") {
    it("should parse correctly") {
      val json = Json {
        ignoreUnknownKeys = true
      }
      val response = json.decodeFromString<LovedTracksResponse>(rawJson)
      response.lovedTracks.tracks.size shouldBe 50
    }
  }
})
