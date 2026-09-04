package com.mataku.scrobscrob.data.db

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class SessionBackupPayloadSpec : DescribeSpec({
  describe("toJsonBytes / fromJsonBytes") {
    it("round-trips sessionKey and username") {
      val payload = SessionBackupPayload(
        sessionKey = "abcdef0123456789",
        username = "matakucom",
      )

      SessionBackupPayload.fromJsonBytes(payload.toJsonBytes()) shouldBe payload
    }

    it("returns null for malformed bytes") {
      SessionBackupPayload.fromJsonBytes("not json".encodeToByteArray()).shouldBeNull()
    }
  }
})
