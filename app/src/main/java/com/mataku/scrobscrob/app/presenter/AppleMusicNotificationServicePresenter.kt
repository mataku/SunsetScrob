package com.mataku.scrobscrob.app.presenter

import android.util.Log
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.api.service.TrackInfoService
import com.mataku.scrobscrob.app.model.api.service.TrackScrobbleService
import com.mataku.scrobscrob.app.model.api.service.TrackUpdateNowPlayingService
import com.mataku.scrobscrob.app.model.entity.NowPlayingApiResponse
import com.mataku.scrobscrob.app.model.entity.ScrobblesApiResponse
import com.mataku.scrobscrob.app.model.entity.TrackInfoApiResponse
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.AppUtil
import kotlinx.coroutines.experimental.runBlocking
import retrofit2.Response
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResponse
import ru.gildor.coroutines.retrofit.awaitResult

class AppleMusicNotificationServicePresenter(var notificationServiceInterface: NotificationServiceInterface) {

    private val appUtil = AppUtil()
    private val apiKey = appUtil.apiKey
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
        params["api_key"] = appUtil.apiKey

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.create(TrackScrobbleService::class.java)
        val result: Result<ScrobblesApiResponse> = client.scrobble(
                track.artistName,
                track.name,
                timeStamp,
                track.albumName,
                appUtil.apiKey,
                apiSig,
                sessionKey
        ).awaitResult()
        when (result) {
            is Result.Ok -> notificationServiceInterface.saveScrobble(track)
            is Result.Error -> {
                if (BuildConfig.DEBUG) {
                    Log.i("ScrobbleError", "Error")
                }
            }
            is Result.Exception -> {
                if (BuildConfig.DEBUG) {
                    Log.i("ScrobbleError", result.exception.localizedMessage)
                }
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
        params["api_key"] = appUtil.apiKey

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.create(TrackUpdateNowPlayingService::class.java)
        try {
            val apiResponse: Response<NowPlayingApiResponse> = client.updateNowPlaying(
                    track.artistName,
                    track.name,
                    track.albumName,
                    appUtil.apiKey,
                    apiSig,
                    sessionKey
            ).awaitResponse()
            if (apiResponse.isSuccessful) {
                notificationServiceInterface.notifyNowPlayingUpdated(track)
            }
        } catch (e: Throwable) {
            if (BuildConfig.DEBUG) {
                Log.i("NowPlayingApi", e.printStackTrace().toString())
            }
        }
    }

    private fun getTrackInfo(artistName: String, trackName: String) = runBlocking {
        val client = Retrofit2LastFmClient.create(TrackInfoService::class.java)
        var trackDuration = appUtil.defaultPlayingTime
        var albumArtwork = ""
        val result: Result<TrackInfoApiResponse> = client.getTrackInfo(
                artistName,
                trackName,
                apiKey
        ).awaitResult()
        when (result) {
            is Result.Ok -> {
                val response = result.value
                trackDuration = response.trackInfo.duration.toLong() / 1000L
                // Use default value if duration is 0
                if (trackDuration == 0L) {
                    trackDuration = appUtil.defaultPlayingTime
                }
                try {
                    val imageList = response.trackInfo.album.imageList
                    albumArtwork = imageList[1].imageUrl
                } catch (e: NullPointerException) {

                }
                notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
            }
            is Result.Error -> notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
            is Result.Exception -> {
                if (BuildConfig.DEBUG) {
                    Log.i("GetTrackInfoApi", result.exception.localizedMessage)
                }
            }
        }
    }
}