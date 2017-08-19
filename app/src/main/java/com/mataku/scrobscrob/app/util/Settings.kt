package com.mataku.scrobscrob.app.util

import com.mataku.scrobscrob.BuildConfig

class Settings {
    val apiKey = BuildConfig.API_KEY
    val sharedSecret = BuildConfig.SHARED_SECRET
    val defaultPlayingTime = 180.toLong()
}