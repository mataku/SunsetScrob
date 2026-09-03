package com.mataku.scrobscrob.app.testing

import android.content.Context
import com.mataku.scrobscrob.app.App
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

internal fun resetDataStores(context: Context) {
  val app = context.applicationContext as App
  runBlocking {
    app.appGraph.sessionRepository.logout().collect()
  }
}
