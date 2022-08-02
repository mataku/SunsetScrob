package com.mataku.scrobscrob.app.model

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// singleton instance
@ExperimentalCoroutinesApi
object RxEventBus {

  val bus = ConflatedBroadcastChannel<Any>()

  fun send(o: Any) {
    GlobalScope.launch {
      bus.send(o)
    }
  }

  inline fun <reified T> asChannel(): Flow<T> {
    return bus.openSubscription().consumeAsFlow().filter { it is T }.map { it as T }
  }
}
