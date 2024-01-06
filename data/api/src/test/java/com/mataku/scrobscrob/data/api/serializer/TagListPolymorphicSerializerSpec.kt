package com.mataku.scrobscrob.data.api.serializer

import com.mataku.scrobscrob.data.api.model.MultipleTag
import com.mataku.scrobscrob.data.api.model.SingleTag
import com.mataku.scrobscrob.data.api.model.TagListBody
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class TagListPolymorphicSerializerSpec : DescribeSpec({
  val json = Json {
    ignoreUnknownKeys = true
  }
  describe("list response") {
    it("should parse as tag list") {
      val rawJson = """
        {
          "tags": {
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
      }
      """.trimIndent()

      val tagsBody = json.decodeFromString<TestTag>(rawJson)
      (tagsBody.tags is MultipleTag) shouldBe true
    }
  }

  describe("single response") {
//    context("tag entity") {
//      it("should parse as emptyList") {
//        val rawJson = """
//        {
//          "tags": {
//            "tag": {
//              "url": "https://www.last.fm/tag/2023",
//              "name": "2023"
//            }
//          }
//        }
//      """.trimIndent()
//      }
//    }
    context("empty string") {
      it("should parse as emptyList") {
        val rawJson = """
          {
            "tags": ""
          }
        """.trimIndent()
        val tagsBody = json.decodeFromString<TestTag>(rawJson)
        (tagsBody.tags is SingleTag) shouldBe true
      }
    }
  }
}
)

@Serializable
private data class TestTag(
  @SerialName("tags")
  val tags: TagListBody
)
