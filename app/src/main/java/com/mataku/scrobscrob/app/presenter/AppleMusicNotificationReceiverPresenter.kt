package com.mataku.scrobscrob.app.presenter

import android.content.Intent
import android.util.Log
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.entity.NowPlayingApiResponse
import com.mataku.scrobscrob.app.ui.view.NotificationInterface
import com.mataku.scrobscrob.app.util.Settings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class AppleMusicNotificationReceiverPresenter(var notificationInterface: NotificationInterface) {
    private val appSettings = Settings()
    private val nowPlayingMethod = "track.updateNowPlaying"

    fun setNowPlayingIfDifferentTrack(track: Track?, intent: Intent, sessionKey: String) {
        val newTrackName = intent.getStringExtra("trackName")

        if (track == null) {
            val newTrack = createTrack(intent)
            setNowPlaying(newTrack, sessionKey)
            notificationInterface.updateCurrentTrack(intent)
        } else if (track.name != newTrackName) {
            val newTrack = createTrack(intent)
            setNowPlaying(newTrack, sessionKey)
            notificationInterface.updateCurrentTrack(intent)
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
                appSettings.apiKey,
                apiSig,
                sessionKey
        )

        call.enqueue(object : Callback<NowPlayingApiResponse> {
            override fun onResponse(call: Call<NowPlayingApiResponse>?, response: Response<NowPlayingApiResponse>?) {
                if (response!!.isSuccessful) {
                    val body = response.body()
                    System.out.println(body)
                    Log.i("NowPlayingApi", "Success to update NowPlaying")
                } else {
                    Log.i("NowPlayingApi", "Something wrong")
                    println(response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<NowPlayingApiResponse>?, t: Throwable?) {
                Log.i("NowPlayingApi", "Failure")
            }
        })
    }

    private fun generateApiSig(sessionKey: String, track: Track): String {
        var str = "album${track.albumName}api_key${appSettings.apiKey}artist${track.artistName}method${nowPlayingMethod}"
        str += "sk${sessionKey}track${track.name}${appSettings.sharedSecret}"
        val md = MessageDigest.getInstance("MD5")
        val data = str.toByteArray()
        md.update(data)
        val digest = md.digest()
        val stringBuilder = StringBuilder()
        digest.indices.forEach { i ->
            val b = (0xFF and digest[i].toInt())
            if (b < 16)
                stringBuilder.append("0")
            stringBuilder.append(Integer.toHexString(b))
        }
        return stringBuilder.toString()
    }

    private fun createTrack(intent: Intent): Track {
        val artistName = intent.getStringExtra("artistName")
        val trackName = intent.getStringExtra("trackName")
        val albumName = intent.getStringExtra("albumName")
        val playingTime = intent.getLongExtra("playingTime", appSettings.defaultPlayingTime)
        val timeStamp = intent.getLongExtra("timeStamp", System.currentTimeMillis())
        return Track(artistName, trackName, albumName, playingTime, timeStamp)
    }

    private fun overScrobblingPoint(timeStamp: Long, playingTime: Long): Boolean {
        val now = System.currentTimeMillis()
        val scrobblingPoint = playingTime / 2
        return (now - timeStamp) > scrobblingPoint
    }
}