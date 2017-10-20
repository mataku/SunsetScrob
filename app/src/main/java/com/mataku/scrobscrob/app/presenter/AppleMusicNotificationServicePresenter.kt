package com.mataku.scrobscrob.app.presenter

import android.util.Log
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.api.service.TrackInfoService
import com.mataku.scrobscrob.app.model.api.service.TrackScrobbleService
import com.mataku.scrobscrob.app.model.api.service.TrackUpdateNowPlayingService
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.AppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppleMusicNotificationServicePresenter(var notificationServiceInterface: NotificationServiceInterface) {

    private val appUtil = AppUtil()
    private val apiKey = appUtil.apiKey
    private val scrobbleMethod = "track.scrobble"
    private val nowPlayingMethod = "track.updateNowPlaying"

    fun getTrackInfo(track: Track, sessionKey: String) {
        setNowPlaying(track, sessionKey)
        getTrackInfo(track.artistName, track.name)
    }

    fun scrobble(track: Track, sessionKey: String, timeStamp: Long) {
        val params: MutableMap<String, String> = mutableMapOf()
        params["artist[0]"] = track.artistName
        params["track[0]"] = track.name
        params["timestamp[0]"] = timeStamp.toString()
        params["album[0]"] = track.albumName
        params["method"] = scrobbleMethod
        params["sk"] = sessionKey
        params["api_key"] = appUtil.apiKey

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.create(TrackScrobbleService::class.java)
        client.scrobble(
                track.artistName,
                track.name,
                timeStamp,
                track.albumName,
                appUtil.apiKey,
                apiSig,
                sessionKey
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful && result.body() != null && result.body()!!.scrobbles.attr.accepted == 1) {
                        notificationServiceInterface.saveScrobble(track)
                        if (BuildConfig.DEBUG) {
                            Log.i("scrobbleApi", "success")
                        }
                    }
                }, { error ->
                    if (BuildConfig.DEBUG) {
                        Log.i("Scrobble API", error.message)
                    }
                })
    }

    private fun setNowPlaying(track: Track, sessionKey: String) {
        val params: MutableMap<String, String> = mutableMapOf()
        params["artist"] = track.artistName
        params["track"] = track.name
        params["album"] = track.albumName
        params["method"] = nowPlayingMethod
        params["sk"] = sessionKey
        params["api_key"] = appUtil.apiKey

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.create(TrackUpdateNowPlayingService::class.java)
        client.updateNowPlaying(
                track.artistName,
                track.name,
                track.albumName,
                appUtil.apiKey,
                apiSig,
                sessionKey
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        if (BuildConfig.DEBUG) {
                            Log.i("NowPlayingApi", "success")
                        }
                        notificationServiceInterface.notifyNowPlayingUpdated(track)
                    }
                }, { _ ->
                    if (BuildConfig.DEBUG) {
                        Log.i("NowPlayingApi", "Something wrong")
                    }
                })

    }

    private fun getTrackInfo(artistName: String, trackName: String) {
        val client = Retrofit2LastFmClient.create(TrackInfoService::class.java)
        var trackDuration = appUtil.defaultPlayingTime
        var albumArtwork = ""
        client.getTrackInfo(
                artistName,
                trackName,
                apiKey
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful && result.body() != null) {
                        val response = result.body()
                        trackDuration = response!!.trackInfo.duration.toLong() / 1000L
                        try {
                            val imageList = response.trackInfo.album.imageList
                            albumArtwork = response.trackInfo.album.imageList[1].imageUrl
                        } catch (e: NullPointerException) {

                        }
                        // Use default value if duration is 0
                        if (trackDuration == 0L) {
                            trackDuration = appUtil.defaultPlayingTime
                        }
                    }
                    notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
                }, { _ ->
                    notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
                })

    }
}