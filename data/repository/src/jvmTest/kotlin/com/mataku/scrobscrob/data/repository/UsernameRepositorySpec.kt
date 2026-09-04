package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.data.db.UsernameDataStore
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class UsernameRepositorySpec : DescribeSpec({
  describe("asyncUsername") {
    it("emits the current username from the data store") {
      val usernameDataStore = mockk<UsernameDataStore>()
      coEvery { usernameDataStore.username() } returns "matakucom"

      val repository = UsernameRepositoryImpl(usernameDataStore)

      repository.asyncUsername().test {
        awaitItem() shouldBe "matakucom"
        awaitComplete()
      }
    }
  }

  describe("usernameFlow") {
    it("delegates straight to UsernameDataStore.usernameFlow()") {
      val usernameDataStore = mockk<UsernameDataStore>()
      every { usernameDataStore.usernameFlow() } returns flowOf("matakucom")

      val repository = UsernameRepositoryImpl(usernameDataStore)

      repository.usernameFlow().test {
        awaitItem() shouldBe "matakucom"
        awaitComplete()
      }
    }
  }
})
