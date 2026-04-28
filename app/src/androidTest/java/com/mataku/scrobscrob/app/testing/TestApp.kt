package com.mataku.scrobscrob.app.testing

import com.mataku.scrobscrob.app.App
import com.mataku.scrobscrob.app.di.AppGraphContract
import dev.zacsweers.metro.createGraphFactory

class TestApp : App() {
  override fun newAppGraph(): AppGraphContract =
    createGraphFactory<TestAppGraph.Factory>().create(this)
}
