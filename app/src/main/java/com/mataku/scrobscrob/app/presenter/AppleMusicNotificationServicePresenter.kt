package com.mataku.scrobscrob.app.presenter

import android.util.Log
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.entity.AlbumInfoApiResponse
import com.mataku.scrobscrob.app.model.entity.TrackInfoApiResponse
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.Settings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppleMusicNotificationServicePresenter(var notificationServiceInterface: NotificationServiceInterface) {

    private val settings = Settings()
    private val apiKey = settings.apiKey

    fun getTrackDuration(artistName: String, trackName: String): Long {
        val client = Retrofit2LastFmClient.createService()
        val call = client.getTrackInfo(
                artistName,
                trackName,
                apiKey
        )

        var trackDuration = settings.defaultPlayingTime

        call.enqueue(object : Callback<TrackInfoApiResponse> {
            override fun onResponse(call: Call<TrackInfoApiResponse>?, response: Response<TrackInfoApiResponse>?) {
                if (response!!.isSuccessful) {
                    if (response.body() != null) {
                        val body = response.body()
                        trackDuration = body?.trackInfo?.duration!!.toLong()
                        // Use default value if duration is 0
                        if (trackDuration == 0L) {
                            trackDuration = settings.defaultPlayingTime
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

    fun getAlbumArtWork(albumName: String, artistName: String, trackName: String): String {
        val client = Retrofit2LastFmClient.createService()
        val call = client.getAlbumInfo(
                albumName,
                artistName,
                trackName,
                apiKey
        )

        var mediumSizeUrl = ""

        call.enqueue(object : Callback<AlbumInfoApiResponse> {
            override fun onResponse(call: Call<AlbumInfoApiResponse>?, response: Response<AlbumInfoApiResponse>?) {
                if (response!!.isSuccessful && response.body() != null) {
                    mediumSizeUrl = response.body()!!.albumInfo.imageList[1].imageUrl
                } else {
                    Log.i("AlbumInfoApi", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<AlbumInfoApiResponse>?, t: Throwable?) {
                Log.i("AlbumInfoApi", "Failure")
            }
        })
        return mediumSizeUrl
    }
}