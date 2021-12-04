package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.BuildConfig
import java.security.MessageDigest

class ApiSignature {
    companion object {
        @JvmStatic
        fun generateApiSig(params: MutableMap<String, String>): String {
            var str = ""
            params["api_key"] = BuildConfig.API_KEY
            params.toSortedMap().forEach { (k, v) ->
                str += k + v
            }
            str += BuildConfig.SHARED_SECRET

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
}