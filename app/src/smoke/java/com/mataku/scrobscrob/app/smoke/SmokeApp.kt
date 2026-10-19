package com.mataku.scrobscrob.app.smoke

import com.mataku.scrobscrob.app.App
import com.mataku.scrobscrob.app.di.AppGraphContract
import dev.zacsweers.metro.createGraphFactory

class SmokeApp : App() {
  override fun newAppGraph(): AppGraphContract =
    createGraphFactory<SmokeAppGraph.Factory>().create(this)
}
