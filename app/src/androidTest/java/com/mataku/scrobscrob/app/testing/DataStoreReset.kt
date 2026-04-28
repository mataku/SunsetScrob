package com.mataku.scrobscrob.app.testing

import android.content.Context

internal fun resetDataStores(context: Context) {
  val datastoreDir = context.filesDir.resolve("datastore")
  if (!datastoreDir.exists()) return
  datastoreDir.listFiles()?.forEach { file ->
    if (file.isFile) file.delete()
  }
}
