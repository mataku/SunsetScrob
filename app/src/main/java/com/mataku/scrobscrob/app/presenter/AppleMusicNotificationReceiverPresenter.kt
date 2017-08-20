package com.mataku.scrobscrob.app.presenter

import android.content.Intent
import android.util.Log
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.entity.NowPlayingApiResponse
import com.mataku.scrobscrob.app.model.entity.ScrobblesApiResponse
import com.mataku.scrobscrob.app.ui.view.NotificationInterface
import com.mataku.scrobscrob.app.util.AppUtil
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppleMusicNotificationReceiverPresenter(var notificationInterface: NotificationInterface) {
    private val appUtil = AppUtil()
    private val nowPlayingMethod = "track.updateNowPlaying"
    private val scrobbleMethod = "track.scrobble"

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
            if (overScrobblingPoint(track.timeStamp, track.playingTime)) {
                scrobble(track, sessionKey)
            }
        } else {
            // Do nothing
        }
    }

    private fun setNowPlaying(track: Track, sessionKey: String) {
        var params: MutableMap<String, String> = mutableMapOf()
        params["artist"] = track.artistName
        params["track"] = track.name
        params["album"] = track.albumName
        params["method"] = nowPlayingMethod
        params["sk"] = sessionKey
        params["api_key"] = appUtil.apiKey

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.createService()
        val call = client.updateNowPlaying(
                track.artistName,
                track.name,
                track.albumName,
                appUtil.apiKey,
                apiSig,
                sessionKey
        )

        call.enqueue(object : Callback<NowPlayingApiResponse> {
            override fun onResponse(call: Call<NowPlayingApiResponse>?, response: Response<NowPlayingApiResponse>?) {
                if (response!!.isSuccessful) {
                    Log.i("NowPlayingApi", "success")
                } else {
                    Log.i("NowPlayingApi", "Something wrong")
                }
            }

            override fun onFailure(call: Call<NowPlayingApiResponse>?, t: Throwable?) {
                Log.i("NowPlayingApi", "Failure")
            }
        })
    }

    private fun scrobble(track: Track, sessionKey: String) {
        var params: MutableMap<String, String> = mutableMapOf()
        params["artist[0]"] = track.artistName
        params["track[0]"] = track.name
        params["timestamp[0]"] = track.timeStamp.toString()
        params["album[0]"] = track.albumName
        params["method"] = scrobbleMethod
        params["sk"] = sessionKey
        params["api_key"] = appUtil.apiKey

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.createService()
        val call = client.scrobble(
                track.artistName,
                track.name,
                track.timeStamp,
                track.albumName,
                appUtil.apiKey,
                apiSig,
                sessionKey
        )

        call.enqueue(object : Callback<ScrobblesApiResponse> {
            override fun onResponse(call: Call<ScrobblesApiResponse>?, response: Response<ScrobblesApiResponse>?) {
                if (response!!.isSuccessful && response.body() != null && response.body()!!.scrobbles.attr.accepted == 1) {
                    var realm = Realm.getDefaultInstance();
                    realm.executeTransaction {
                        var scrobble = realm.createObject(Scrobble::class.java, Scrobble().count() + 1)
                        scrobble.albumName = track.albumName
                        scrobble.artistName = track.artistName
                        scrobble.artwork = track.albumArtWork
                        scrobble.timeStamp = track.timeStamp
                        scrobble.trackName = track.name
                    }
                    Log.i("scrobblingApi", "success")
                } else {
                    Log.i("ScrobblingApi", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<ScrobblesApiResponse>?, t: Throwable?) {
                Log.i("ScrobblingApi", "Failure")
            }
        })
    }

    private fun createTrack(intent: Intent): Track {
        val artistName = intent.getStringExtra("artistName")
        val trackName = intent.getStringExtra("trackName")
        val albumName = intent.getStringExtra("albumName")
        val playingTime = intent.getLongExtra("playingTime", appUtil.defaultPlayingTime)
        val timeStamp = intent.getLongExtra("timeStamp", System.currentTimeMillis() / 1000L)
        val albumArtWork = intent.getStringExtra("albumArtWork")
        return Track(artistName, trackName, albumName, playingTime, timeStamp, albumArtWork)
    }

    private fun overScrobblingPoint(timeStamp: Long, playingTime: Long): Boolean {
        val now = System.currentTimeMillis() / 1000L
        val scrobblingPoint = playingTime / 2
        return (now - timeStamp) > scrobblingPoint
    }
}