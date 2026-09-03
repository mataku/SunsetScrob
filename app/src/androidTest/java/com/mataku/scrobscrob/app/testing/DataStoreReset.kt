package com.mataku.scrobscrob.app.testing

import android.content.Context
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import kotlinx.coroutines.runBlocking

internal fun resetDataStores(context: Context) {
  runBlocking {
    UsernameDataStore(context).remove()
    ScrobbleAppDataStore(context).clear()
  }
}
