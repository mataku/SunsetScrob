package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.entity.TrackInfoApiResponse
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.Settings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppleMusicNotificationServicePresenter(var notificationServiceInterface: NotificationServiceInterface) {

    private val settings = Settings()
    private val apiKey = settings.apiKey
    private var trackName: String? = null

    fun getTrackInfo(track: Track) {
        if (trackName == track.name) {
            return
        }
        val client = Retrofit2LastFmClient.createService()
        val call = client.getTrackInfo(
                track.artistName,
                track.name,
                apiKey
        )
        trackName = track.name

        call.enqueue(object : Callback<TrackInfoApiResponse> {
            override fun onResponse(call: Call<TrackInfoApiResponse>?, response: Response<TrackInfoApiResponse>?) {
                if (response!!.isSuccessful) {
                    if (response.body() != null) {
                        val body = response.body()
                        val playingTime = body?.trackInfo?.duration!!.toLong()
                        track.playingTime = playingTime
                        notificationServiceInterface.sendTrackInfoToReceiver(track)
                    } else {
                        notificationServiceInterface.sendTrackInfoToReceiver(track)
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<TrackInfoApiResponse>?, t: Throwable?) {

            }
        })
    }
}