package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.LastFmApiClient
import com.mataku.scrobscrob.app.model.api.service.TrackInfoService
import com.mataku.scrobscrob.app.model.api.service.TrackScrobbleService
import com.mataku.scrobscrob.app.model.api.service.TrackUpdateNowPlayingService
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.AppUtil
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class AppleMusicNotificationServicePresenter(private var notificationServiceInterface: NotificationServiceInterface) {

    private val appUtil = AppUtil()
    private val scrobbleMethod = "track.scrobble"
    private val nowPlayingMethod = "track.updateNowPlaying"

    private val job = Job()

    fun dispose() {
        job.cancel()
    }

    fun getTrackInfo(track: Track, sessionKey: String) {
        launch(job + UI) {
            setNowPlaying(track, sessionKey)
            getTrackInfo(track.artistName, track.name)
        }
    }

    fun scrobble(track: Track, sessionKey: String, timeStamp: Long) {
        launch(job + UI) {
            requestScrobble(track, sessionKey, timeStamp)
        }
    }

    private suspend fun requestScrobble(track: Track, sessionKey: String, timeStamp: Long) {
        val params: MutableMap<String, String> = mutableMapOf()
        params["artist[0]"] = track.artistName
        params["track[0]"] = track.name
        params["timestamp[0]"] = timeStamp.toString()
        params["album[0]"] = track.albumName
        params["method"] = scrobbleMethod
        params["sk"] = sessionKey

        val apiSig = appUtil.generateApiSig(params)
        val client = LastFmApiClient.create(TrackScrobbleService::class.java)
        val result = client.scrobble(
                track.artistName,
                track.name,
                timeStamp,
                track.albumName,
                apiSig,
                sessionKey
        ).await()
        when (result.code()) {
            200, 201 -> {
                result.body()?.scrobbles.let {
                    val accepted = it?.attr?.accepted
                    if (accepted != null && accepted == 1) {
                        notificationServiceInterface.saveScrobble(track)

                        appUtil.debugLog("scrobbleApi", "success")

                    }
                }
            }
            else -> {
                appUtil.debugLog("Scrobble API", result.errorBody().toString())
            }
        }
    }

    private suspend fun setNowPlaying(track: Track, sessionKey: String) {
        val params: MutableMap<String, String> = mutableMapOf()
        params["artist"] = track.artistName
        params["track"] = track.name
        params["album"] = track.albumName
        params["method"] = nowPlayingMethod
        params["sk"] = sessionKey

        val apiSig = appUtil.generateApiSig(params)
        val client = LastFmApiClient.create(TrackUpdateNowPlayingService::class.java)
        val result = client.updateNowPlaying(
                track.artistName,
                track.name,
                track.albumName,
                apiSig,
                sessionKey
        ).await()
        when (result.code()) {
            200, 201 -> {
                appUtil.debugLog("NowPlayingApi", "success")
                notificationServiceInterface.notifyNowPlayingUpdated(track)
            }
            else -> {
                appUtil.debugLog("NowPlayingApi", "Something wrong")
            }
        }
    }

    private suspend fun getTrackInfo(artistName: String, trackName: String) {
        val client = LastFmApiClient.create(TrackInfoService::class.java)
        var trackDuration = appUtil.defaultPlayingTime
        var albumArtwork = ""

        val result = client.getTrackInfo(artistName, trackName).await()
        when (result.code()) {
            200, 201 -> {
                val apiResponse = result.body()
                apiResponse?.trackInfo.let {
                    val duration = it?.duration
                    if (duration != null) {
                        trackDuration = duration.toLong() / 1000L
                    }
                    try {
                        val imageList = it?.album?.imageList
                        albumArtwork = imageList!![1].imageUrl
                    } catch (e: NullPointerException) {

                    }
                }
                // Use default value if duration is 0
                if (trackDuration == 0L) {
                    trackDuration = appUtil.defaultPlayingTime
                }

                notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
            }
            else -> {
                notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
            }
        }
    }
}