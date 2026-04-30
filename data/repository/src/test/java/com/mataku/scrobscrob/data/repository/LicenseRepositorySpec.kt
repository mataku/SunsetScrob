package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.data.repository.di.LicenseInfoProvider
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class LicenseRepositorySpec : DescribeSpec({
  describe("licenseList") {
    it("parses the raw license JSON, sorts by name, and appends the Noto Sans entry") {
      val rawJson = """
        [
          {
            "groupId": "com.example",
            "artifactId": "zeta",
            "version": "1.0.0",
            "name": "Zeta Library",
            "spdxLicenses": [
              { "identifier": "Apache-2.0", "name": "Apache 2.0", "url": "https://example.com/apache" }
            ],
            "scm": { "url": "https://github.com/example/zeta" }
          },
          {
            "groupId": "com.example",
            "artifactId": "alpha",
            "version": "1.0.0",
            "name": "Alpha Library",
            "spdxLicenses": [
              { "identifier": "MIT", "name": "MIT", "url": "https://example.com/mit" }
            ],
            "scm": null
          }
        ]
      """.trimIndent()
      val provider = mockk<LicenseInfoProvider>()
      coEvery { provider.licenseRawString() } returns rawJson

      val repository = LicenseRepositoryImpl(provider)

      repository.licenseList().test {
        val list = awaitItem()
        // Sorted alphabetically by name; Noto Sans is appended last.
        list.size shouldBe 3
        list[0].name shouldBe "Alpha Library"
        list[1].name shouldBe "Zeta Library"
        list[1].scm?.url shouldBe "https://github.com/example/zeta"
        list[2].name shouldBe "Noto Sans"
        awaitComplete()
      }
    }

    it("returns the cached list on subsequent calls without re-reading the provider") {
      val rawJson = """
        [
          {
            "groupId": "com.example",
            "artifactId": "alpha",
            "version": "1.0.0",
            "name": "Alpha Library",
            "spdxLicenses": [],
            "scm": null
          }
        ]
      """.trimIndent()
      val provider = mockk<LicenseInfoProvider>()
      coEvery { provider.licenseRawString() } returns rawJson

      val repository = LicenseRepositoryImpl(provider)

      repository.licenseList().test {
        awaitItem()
        awaitComplete()
      }
      repository.licenseList().test {
        val list = awaitItem()
        list.first().name shouldBe "Alpha Library"
        awaitComplete()
      }

      coVerify(exactly = 1) { provider.licenseRawString() }
    }

    it("falls back to just the Noto Sans entry when the raw string is null") {
      val provider = mockk<LicenseInfoProvider>()
      coEvery { provider.licenseRawString() } returns null

      val repository = LicenseRepositoryImpl(provider)

      repository.licenseList().test {
        val list = awaitItem()
        list.size shouldBe 1
        list[0].name shouldBe "Noto Sans"
        awaitComplete()
      }
    }
  }
})
