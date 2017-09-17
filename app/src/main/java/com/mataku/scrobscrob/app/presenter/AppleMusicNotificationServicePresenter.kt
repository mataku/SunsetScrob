package com.mataku.scrobscrob.app.presenter

import android.util.Log
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.entity.AlbumInfoApiResponse
import com.mataku.scrobscrob.app.model.entity.NowPlayingApiResponse
import com.mataku.scrobscrob.app.model.entity.ScrobblesApiResponse
import com.mataku.scrobscrob.app.model.entity.TrackInfoApiResponse
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.AppUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppleMusicNotificationServicePresenter(var notificationServiceInterface: NotificationServiceInterface) {

    private val appUtil = AppUtil()
    private val apiKey = appUtil.apiKey
    private val scrobbleMethod = "track.scrobble"
    private val nowPlayingMethod = "track.updateNowPlaying"

    fun getTrackInfo(track: Track, sessionKey: String) {
        setNowPlaying(track, sessionKey)
        getTrackDuration(track.artistName, track.name)
        getAlbumArtWork(track.albumName, track.artistName, track.name)
    }

    fun scrobble(track: Track, sessionKey: String, timeStamp: Long) {
        var params: MutableMap<String, String> = mutableMapOf()
        params["artist[0]"] = track.artistName
        params["track[0]"] = track.name
        params["timestamp[0]"] = timeStamp.toString()
        params["album[0]"] = track.albumName
        params["method"] = scrobbleMethod
        params["sk"] = sessionKey
        params["api_key"] = appUtil.apiKey

        val apiSig = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.createService()
        val call = client.scrobble(
                track.artistName,
                track.name,
                timeStamp,
                track.albumName,
                appUtil.apiKey,
                apiSig,
                sessionKey
        )

        call.enqueue(object : Callback<ScrobblesApiResponse> {
            override fun onResponse(call: Call<ScrobblesApiResponse>?, response: Response<ScrobblesApiResponse>?) {
                if (response!!.isSuccessful && response.body() != null && response.body()!!.scrobbles.attr.accepted == 1) {
                    notificationServiceInterface.saveScrobble(track)
                    if (BuildConfig.DEBUG) {
                        Log.i("scrobblingApi", "success")
                    }
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.i("ScrobblingApi", "Something went wrong")
                    }
                }
            }

            override fun onFailure(call: Call<ScrobblesApiResponse>?, t: Throwable?) {
                if (BuildConfig.DEBUG) {
                    Log.i("ScrobblingApi", "Failure")
                }
            }
        })
    }

    private fun getTrackDuration(artistName: String, trackName: String): Long {
        val client = Retrofit2LastFmClient.createService()
        val call = client.getTrackInfo(
                artistName,
                trackName,
                apiKey
        )

        var trackDuration = appUtil.defaultPlayingTime

        call.enqueue(object : Callback<TrackInfoApiResponse> {
            override fun onResponse(call: Call<TrackInfoApiResponse>?, response: Response<TrackInfoApiResponse>?) {
                if (response!!.isSuccessful) {
                    if (response.body() != null && response.body()?.trackInfo != null) {
                        val body = response.body()
                        trackDuration = body?.trackInfo?.duration!!.toLong()
                        // Use default value if duration is 0
                        if (trackDuration == 0L) {
                            trackDuration = appUtil.defaultPlayingTime
                        }
                    }
                } else {
                    // Do nothing
                }
            }

            override fun onFailure(call: Call<TrackInfoApiResponse>?, t: Throwable?) {

            }
        })

        return trackDuration
    }

    private fun getAlbumArtWork(albumName: String, artistName: String, trackName: String) {
        val client = Retrofit2LastFmClient.createService()
        val call = client.getAlbumInfo(
                albumName,
                artistName,
                trackName,
                apiKey
        )

        var largeSizeUrl = ""

        call.enqueue(object : Callback<AlbumInfoApiResponse> {
            override fun onResponse(call: Call<AlbumInfoApiResponse>?, response: Response<AlbumInfoApiResponse>?) {
                if (response!!.isSuccessful && response.body() != null && response.body()?.albumInfo != null) {
                    largeSizeUrl = response.body()!!.albumInfo.imageList[2].imageUrl
                    notificationServiceInterface.setAlbumArtwork(largeSizeUrl)
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.i("AlbumInfoApi", "Something went wrong")
                    }
                    notificationServiceInterface.setAlbumArtwork(largeSizeUrl)
                }
            }

            override fun onFailure(call: Call<AlbumInfoApiResponse>?, t: Throwable?) {
                if (BuildConfig.DEBUG) {
                    Log.i("AlbumInfoApi", "Failure")
                }
                notificationServiceInterface.setAlbumArtwork(largeSizeUrl)
            }
        })
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
                    if (com.mataku.scrobscrob.BuildConfig.DEBUG) {
                        Log.i("NowPlayingApi", "success")
                    }

                } else {
                    if (BuildConfig.DEBUG) {
                        Log.i("NowPlayingApi", "Something wrong")
                    }
                }
            }

            override fun onFailure(call: Call<NowPlayingApiResponse>?, t: Throwable?) {
                if (BuildConfig.DEBUG) {
                    Log.i("NowPlayingApi", "Failure")
                }
            }
        })
    }
}