package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.api.service.TrackInfoService
import com.mataku.scrobscrob.app.model.api.service.TrackScrobbleService
import com.mataku.scrobscrob.app.model.api.service.TrackUpdateNowPlayingService
import com.mataku.scrobscrob.app.model.entity.NowPlayingApiResponse
import com.mataku.scrobscrob.app.model.entity.ScrobblesApiResponse
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.AppUtil
import kotlinx.coroutines.experimental.runBlocking
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class AppleMusicNotificationServicePresenter(var notificationServiceInterface: NotificationServiceInterface) {

    private val appUtil = AppUtil()
    private val scrobbleMethod = "track.scrobble"
    private val nowPlayingMethod = "track.updateNowPlaying"

    fun getTrackInfo(track: Track, sessionKey: String) {
        setNowPlaying(track, sessionKey)
        getTrackInfo(track.artistName, track.name)
    }

    fun scrobble(track: Track, sessionKey: String, timeStamp: Long) = runBlocking {
        val params: MutableMap<String, String> = mutableMapOf()
        params["artist[0]"] = track.artistName
        params["track[0]"] = track.name
        params["timestamp[0]"] = timeStamp.toString()
        params["album[0]"] = track.albumName
        params["method"] = scrobbleMethod
        params["sk"] = sessionKey

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.create(TrackScrobbleService::class.java)
        val result: Result<ScrobblesApiResponse> = client.scrobble(
                track.artistName,
                track.name,
                timeStamp,
                track.albumName,
                apiSig,
                sessionKey
        ).awaitResult()
        when (result) {
            is Result.Ok -> {
                if (result.value.scrobbles.attr.accepted == 1) {
                    notificationServiceInterface.saveScrobble(track)
                }
            }
            is Result.Error -> {
            }
            is Result.Exception -> {
            }
        }
    }

    private fun setNowPlaying(track: Track, sessionKey: String) = runBlocking {
        val params: MutableMap<String, String> = mutableMapOf()
        params["artist"] = track.artistName
        params["track"] = track.name
        params["album"] = track.albumName
        params["method"] = nowPlayingMethod
        params["sk"] = sessionKey

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.create(TrackUpdateNowPlayingService::class.java)
        val result: Result<NowPlayingApiResponse> = client.updateNowPlaying(
                track.artistName,
                track.name,
                track.albumName,
                apiSig,
                sessionKey
        ).awaitResult()
        when (result) {
            is Result.Ok -> {
                notificationServiceInterface.notifyNowPlayingUpdated(track)
            }
            is Result.Error -> {
            }
            is Result.Exception -> {
            }
        }
    }

    private fun getTrackInfo(artistName: String, trackName: String) = runBlocking {
        val client = Retrofit2LastFmClient.create(TrackInfoService::class.java)
        var trackDuration = appUtil.defaultPlayingTime
        var albumArtwork = ""
        val result = client.getTrackInfo(
                artistName,
                trackName
        ).awaitResult()
        when (result) {
            is Result.Ok -> {
                trackDuration = result.value.trackInfo.duration.toLong() / 1000L
                try {
                    val imageList = result.value.trackInfo.album.imageList
                    albumArtwork = imageList[1].imageUrl
                } catch (e: NullPointerException) {

                }
                // Use default value if duration is 0
                if (trackDuration == 0L) {
                    trackDuration = appUtil.defaultPlayingTime
                }
                notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
            }
            is Result.Error -> {
                notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
            }
            is Result.Exception -> {
                notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
            }
        }
    }
}