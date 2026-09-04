package com.mataku.scrobscrob.data.api.serializer

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date

class DateSerializerSpec : DescribeSpec({
  val json = Json

  val dateFormat = SimpleDateFormat("yyyy/MM/dd")

  describe("deserialize") {
    context("expected format") {
      it("should parse correctly") {
        val rawJson = """
          {
            "published": "18 Jul 2008, 11:00"
          }
        """.trimIndent()

        val wiki = json.decodeFromString<Wiki>(rawJson)
        dateFormat.format(wiki.published) shouldBe "2008/07/18"
      }
    }

    context("unexpected format") {
      it("should parse as default Date") {
        val rawJson = """
        {
          "published": "18 Jul 2008 11:00"
        }
      """.trimIndent()
        val wiki = json.decodeFromString<Wiki>(rawJson)
        dateFormat.format(wiki.published) shouldBe "1970/01/01"
      }
    }
  }
})

@Serializable
private data class Wiki(
  @Serializable(DateSerializer::class)
  val published: Date,
)
