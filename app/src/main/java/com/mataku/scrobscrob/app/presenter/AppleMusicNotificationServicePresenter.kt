package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.LastFmApiClient
import com.mataku.scrobscrob.app.model.api.service.TrackInfoService
import com.mataku.scrobscrob.app.model.api.service.TrackScrobbleService
import com.mataku.scrobscrob.app.model.api.service.TrackUpdateNowPlayingService
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.AppUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AppleMusicNotificationServicePresenter(private var notificationServiceInterface: NotificationServiceInterface) {

    private val appUtil = AppUtil()
    private val scrobbleMethod = "track.scrobble"
    private val nowPlayingMethod = "track.updateNowPlaying"

    private val coroutineContext = Job() + Dispatchers.Main

    fun dispose() {
        coroutineContext.cancel()
    }

    fun getTrackInfo(trackName: String, artistName: String, sessionKey: String) {
        CoroutineScope(coroutineContext).launch {
            setNowPlaying(trackName, artistName, sessionKey)
            getTrackInfo(artistName, trackName)
        }
    }

    fun scrobble(track: Track, sessionKey: String, timeStamp: Long) {
        CoroutineScope(coroutineContext).launch {
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

    private suspend fun setNowPlaying(trackName: String, artistName: String, sessionKey: String) {
        val params: MutableMap<String, String> = mutableMapOf()
        params["artist"] = artistName
        params["track"] = trackName
        params["album"] = ""
        params["method"] = nowPlayingMethod
        params["sk"] = sessionKey

        val apiSig = appUtil.generateApiSig(params)
        val client = LastFmApiClient.create(TrackUpdateNowPlayingService::class.java)
        val result = client.updateNowPlaying(
            artistName,
            trackName,
            "",
            apiSig,
            sessionKey
        ).await()
        when (result.code()) {
            200, 201 -> {
                appUtil.debugLog("NowPlayingApi", "success")
                result.body()?.nowPlaying?.let {
                    notificationServiceInterface.notifyNowPlayingUpdated(
                        Track(
                            it.artist.text,
                            it.track.text,
                            it.album.text
                        )
                    )
                }
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