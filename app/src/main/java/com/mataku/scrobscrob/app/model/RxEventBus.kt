package com.mataku.scrobscrob.app.model

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.filter
import kotlinx.coroutines.channels.map
import kotlinx.coroutines.launch

// singleton instance
@ExperimentalCoroutinesApi
object RxEventBus {

    val bus = ConflatedBroadcastChannel<Any>()

    fun send(o: Any) {
        GlobalScope.launch {
            bus.send(0)
        }
    }

    @ObsoleteCoroutinesApi
    inline fun <reified T> asChannel(): ReceiveChannel<T> {
        return bus.openSubscription().filter { it is T }.map { it as T }
    }
}