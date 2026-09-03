package com.mataku.scrobscrob.auth.webauth

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@SingleIn(AppScope::class)
@Inject
class WebAuthCallbackChannel {
  private val channel = Channel<String>(Channel.CONFLATED)

  val tokens: Flow<String> = channel.receiveAsFlow()

  fun offer(token: String) {
    channel.trySend(token)
  }
}
