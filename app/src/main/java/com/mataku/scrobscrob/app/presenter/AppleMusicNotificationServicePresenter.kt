package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.api.service.TrackInfoService
import com.mataku.scrobscrob.app.model.api.service.TrackScrobbleService
import com.mataku.scrobscrob.app.model.api.service.TrackUpdateNowPlayingService
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.AppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppleMusicNotificationServicePresenter(private var notificationServiceInterface: NotificationServiceInterface) {

    private val appUtil = AppUtil()
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

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.create(TrackScrobbleService::class.java)
        client.scrobble(
                track.artistName,
                track.name,
                timeStamp,
                track.albumName,
                apiSig,
                sessionKey
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful && result?.body() != null) {
                        result.body()?.scrobbles?.attr?.accepted.let {
                            if (it == 1) {
                                notificationServiceInterface.saveScrobble(track)
                                appUtil.debugLog("Scrobble API", "success")
                            }
                        }
                    }
                }, { error ->
                    appUtil.debugLog("Scrobble API", error?.message)
                })
    }

    private fun setNowPlaying(track: Track, sessionKey: String) {
        val params: MutableMap<String, String> = mutableMapOf()
        params["artist"] = track.artistName
        params["track"] = track.name
        params["album"] = track.albumName
        params["method"] = nowPlayingMethod
        params["sk"] = sessionKey

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.create(TrackUpdateNowPlayingService::class.java)
        client.updateNowPlaying(
                track.artistName,
                track.name,
                track.albumName,
                apiSig,
                sessionKey
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        appUtil.debugLog("NowPlayingApi", "success")
                        notificationServiceInterface.notifyNowPlayingUpdated(track)
                    }
                }, { _ ->
                    appUtil.debugLog("NowPlayingApi", "Something wrong")
                })

    }

    private fun getTrackInfo(artistName: String, trackName: String) {
        val client = Retrofit2LastFmClient.create(TrackInfoService::class.java)
        var trackDuration = appUtil.defaultPlayingTime
        var albumArtwork = ""
        client.getTrackInfo(
                artistName,
                trackName
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        result?.body().let {
                            trackDuration = it?.trackInfo?.duration.let { duration ->
                                if (duration!!.toLong() == 0L) {
                                    // Use default value if duration is 0
                                    appUtil.defaultPlayingTime
                                } else {
                                    duration.toLong() / 1000L
                                }
                            }
                            try {
                                val imageList = it?.trackInfo?.album?.imageList
                                albumArtwork = imageList!![1].imageUrl
                            } catch (e: NullPointerException) {

                            }
                        }
                    }
                    notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
                }, { _ ->
                    notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
                })
    }
}