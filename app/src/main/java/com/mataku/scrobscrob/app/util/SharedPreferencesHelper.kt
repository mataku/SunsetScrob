package com.mataku.scrobscrob.app.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
    private val appUtil = AppUtil()

    fun getTimeStamp(): Long =
            sharedPreferences.getLong("TimeStamp", System.currentTimeMillis() / 1000L)

    fun getAlbumArtWork(): String {
        return sharedPreferences.getString("AlbumArtwork", "") ?: return ""
    }

    fun getSessionKey(): String {
        return sharedPreferences.getString("SessionKey", "") ?: return ""
    }

    fun setTimeStamp() {
        val timeStamp = System.currentTimeMillis() / 1000L
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putLong("TimeStamp", timeStamp)
        editor.apply()
    }

    fun setPLayingTime(playingTime: Long) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putLong("PlayingTime", playingTime)
        editor.apply()
    }

    fun setAlbumArtwork(albumArtwork: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("AlbumArtwork", albumArtwork)
        editor.apply()
    }

    fun setSessionKey(sessionKey: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("SessionKey", sessionKey)
        editor.apply()
    }

    fun overScrobblingPoint(): Boolean {
        val now = System.currentTimeMillis() / 1000L
        val scrobblingPoint = getPlayingTime() / 2
        return now - getTimeStamp() > scrobblingPoint
    }

    private fun getPlayingTime(): Long =
            sharedPreferences.getLong("PlayingTime", appUtil.defaultPlayingTime)
}