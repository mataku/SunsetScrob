package com.mataku.scrobscrob.baselineprofile.di

import com.mataku.scrobscrob.data.repository.UsernameRepository

class FakeUsernameRepository : UsernameRepository {
  override fun username(): String = "matakucom"

  override suspend fun asyncUsername(): String {
    println("MATAKUDEBUG fake!!!!!!!!!!!!!")
    return "matakucom"
  }
}
