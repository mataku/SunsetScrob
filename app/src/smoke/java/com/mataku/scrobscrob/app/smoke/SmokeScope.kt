package com.mataku.scrobscrob.app.smoke

// The production AppGraph is compiled into the smoke variant too, so the fakes contribute to this
// scope instead of AppScope; only SmokeAppGraph aggregates it.
abstract class SmokeScope private constructor()
