package com.mataku.scrobscrob.app.util

import com.mataku.scrobscrob.BuildConfig
import java.security.MessageDigest

class AppSettings {
    val apiKey = BuildConfig.API_KEY
    val sharedSecret = BuildConfig.SHARED_SECRET
    val defaultPlayingTime = 180.toLong()
    val latestCcrobbleCountToDisplay = 20

    fun generateApiSig(sessionKey: String, params: MutableMap<String, String>): String {
        var str = ""
        params["api_key"] = apiKey
        params.toSortedMap().forEach { k, v ->
            str += k + v
        }
        str += sharedSecret

        val messageDigest = MessageDigest.getInstance("MD5")
        val byteData = str.toByteArray()
        messageDigest.update(byteData)
        val digest = messageDigest.digest()
        val stringBuilder = StringBuilder()
        digest.indices.forEach { i ->
            val b = (0xFF and digest[i].toInt())
            if (b < 16)
                stringBuilder.append("0")
            stringBuilder.append(Integer.toHexString(b))
        }
        return stringBuilder.toString()
    }
}