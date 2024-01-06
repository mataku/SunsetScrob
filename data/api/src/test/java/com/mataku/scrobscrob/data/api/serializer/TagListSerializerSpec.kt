package com.mataku.scrobscrob.data.api.serializer

import com.mataku.scrobscrob.data.api.model.TagsBody
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class TagListSerializerSpec : DescribeSpec({
  val json = Json {
    ignoreUnknownKeys = true
  }
  describe("list response") {
    it("should parse as tag list") {
      val rawJson = """
        {
              "tag": [
                {
                  "url": "https://www.last.fm/tag/rock",
                  "name": "rock"
                },
                {
                  "url": "https://www.last.fm/tag/alternative",
                  "name": "alternative"
                },
                {
                  "url": "https://www.last.fm/tag/britpop",
                  "name": "britpop"
                },
                {
                  "url": "https://www.last.fm/tag/coldplay",
                  "name": "coldplay"
                },
                {
                  "url": "https://www.last.fm/tag/alternative+rock",
                  "name": "alternative rock"
                }
              ]
            }
      """.trimIndent()

      val tagsBody = json.decodeFromString<TagsBody>(rawJson)
      tagsBody.tagList.isNotEmpty() shouldBe true
    }
  }

  describe("single response") {
    it("should parse as emptyList") {
      val rawJson = """
        {
          "tags": {
            "tag": {
              "url": "https://www.last.fm/tag/2023",
              "name": "2023"
            }
          }
        }
      """.trimIndent()

      val tagsBody = json.decodeFromString<TagsBody>(rawJson)
      tagsBody.tagList.isEmpty() shouldBe true
    }
  }
})
