package com.mataku.scrobscrob.app.presenter

import android.content.Intent
import android.util.Log
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.entity.NowPlayingApiResponse
import com.mataku.scrobscrob.app.ui.view.NotificationInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest

class AppleMusicNotificationReceiverPresenter(var notificationInterface: NotificationInterface) {
    private val apiKey = BuildConfig.API_KEY
    private val sharedSecret = BuildConfig.SHARED_SECRET
    private val method = "track.updateNowPlaying"

    fun setNowPlayingIfDifferentTrack(track: Track?, intent: Intent, sessionKey: String) {
        val newTrackName = intent.getStringExtra("trackName")

        if (track == null) {
            val newTrack = createTrack(intent)
            setNowPlaying(newTrack, sessionKey)
            notificationInterface.updateCurrentTrack(intent)
            Log.i("NowPlaying", "Sent!")
        } else if (track.name != newTrackName) {
            val newTrack = createTrack(intent)
            setNowPlaying(newTrack, sessionKey)
            notificationInterface.updateCurrentTrack(intent)
            Log.i("NowPlaying", "Sent!")
        } else {
            // Do nothing
        }
    }

    private fun setNowPlaying(track: Track, sessionKey: String) {
        val apiSig = generateApiSig(sessionKey, track)
        val client = Retrofit2LastFmClient.createService()
        val call = client.updateNowPlaying(
                track.artistName,
                track.name,
                track.albumName,
                apiKey,
                apiSig,
                sessionKey
        )


        call.enqueue(object : Callback<NowPlayingApiResponse> {
            override fun onResponse(call: Call<NowPlayingApiResponse>?, response: Response<NowPlayingApiResponse>?) {
                if (response!!.isSuccessful) {
                    val body = response.body()
                    System.out.println(body)
                } else {

                }
            }

            override fun onFailure(call: Call<NowPlayingApiResponse>?, t: Throwable?) {
                Log.i("Notification", "Failure")
            }
        })
    }

    private fun generateApiSig(sessionKey: String, track: Track): String {
        val str = "api_key${apiKey}method${method}sk${sessionKey}track${track.name}artist${track.artistName}album${track.albumName}${sharedSecret}"
        var md5Str = ""
        try {
            var strBytes: ByteArray = str.toByteArray(charset("UTF-8"))
            val md = MessageDigest.getInstance("MD5")
            val md5Bytes = md.digest(strBytes)
            val bigInt = BigInteger(1, md5Bytes)
            md5Str = bigInt.toString(16)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return md5Str
    }

    private fun createTrack(intent: Intent): Track {
        val artistName = intent.getStringExtra("artistName")
        val trackName = intent.getStringExtra("trackName")
        val albumName = intent.getStringExtra("albumName")
        val playingTime = intent.getLongExtra("playingTime", 0.toLong())
        return Track(artistName, trackName, albumName, playingTime)
    }
}