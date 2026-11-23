package com.mataku.scrobscrob.data.db

import app.cash.turbine.test
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class UsernameDataStoreSpec : DescribeSpec({
  describe("UsernameDataStore") {
    it("returns null when nothing has been stored") {
      withTestPreferencesDataStore { dataStore ->
        val store = UsernameDataStore(dataStore)

        store.username().shouldBeNull()
        store.usernameFlow().first().shouldBeNull()
      }
    }

    it("persists the value passed to setUsername") {
      withTestPreferencesDataStore { dataStore ->
        val store = UsernameDataStore(dataStore)
        store.setUsername("matakucom").test {
          awaitItem() shouldBe Unit
          awaitComplete()
        }

        store.username() shouldBe "matakucom"
      }
    }

    it("emits the new username to usernameFlow collectors") {
      withTestPreferencesDataStore { dataStore ->
        val store = UsernameDataStore(dataStore)

        store.usernameFlow().test {
          awaitItem().shouldBeNull()
          store.setUsername("matakucom")
          awaitItem() shouldBe "matakucom"
          cancelAndIgnoreRemainingEvents()
        }
      }
    }

    it("clears the stored value on remove") {
      withTestPreferencesDataStore { dataStore ->
        val store = UsernameDataStore(dataStore)
        store.setUsername("matakucom")

        store.remove()

        store.username().shouldBeNull()
      }
    }
  }
})
