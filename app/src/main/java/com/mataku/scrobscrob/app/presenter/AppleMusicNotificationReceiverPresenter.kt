package com.mataku.scrobscrob.app.presenter

import android.content.Intent
import android.util.Log
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.entity.AlbumInfoApiResponse
import com.mataku.scrobscrob.app.model.entity.NowPlayingApiResponse
import com.mataku.scrobscrob.app.model.entity.ScrobblesApiResponse
import com.mataku.scrobscrob.app.ui.view.NotificationInterface
import com.mataku.scrobscrob.app.util.Settings
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class AppleMusicNotificationReceiverPresenter(var notificationInterface: NotificationInterface) {
    private val appSettings = Settings()
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
        val apiSig = generateApiSig(sessionKey, track)
        val client = Retrofit2LastFmClient.createService()
        val call = client.updateNowPlaying(
                track.artistName,
                track.name,
                track.albumName,
                appSettings.apiKey,
                apiSig,
                sessionKey
        )

        call.enqueue(object : Callback<NowPlayingApiResponse> {
            override fun onResponse(call: Call<NowPlayingApiResponse>?, response: Response<NowPlayingApiResponse>?) {
                if (response!!.isSuccessful) {

                } else {
                    Log.i("NowPlayingApi", "Something wrong")
                    println(response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<NowPlayingApiResponse>?, t: Throwable?) {
                Log.i("NowPlayingApi", "Failure")
            }
        })
    }

    private fun scrobble(track: Track, sessionKey: String) {
        val apiSig = generateScrobblingApiSig(sessionKey, track)
        val client = Retrofit2LastFmClient.createService()
        val call = client.scrobble(
                track.artistName,
                track.name,
                track.timeStamp,
                track.albumName,
                appSettings.apiKey,
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

                } else {
                    Log.i("ScrobblingApi", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<ScrobblesApiResponse>?, t: Throwable?) {
                Log.i("ScrobblingApi", "Failure")
            }
        })
    }

    fun getAlbumInfo(albumName: String, artistName: String, trackName: String): String {
        val client = Retrofit2LastFmClient.createService()
        val call = client.getAlbumInfo(
                albumName,
                artistName,
                trackName,
                appSettings.apiKey
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

    private fun generateApiSig(sessionKey: String, track: Track): String {
        var str = "album${track.albumName}api_key${appSettings.apiKey}artist${track.artistName}method${nowPlayingMethod}"
        str += "sk${sessionKey}track${track.name}${appSettings.sharedSecret}"
        val md = MessageDigest.getInstance("MD5")
        val data = str.toByteArray()
        md.update(data)
        val digest = md.digest()
        val stringBuilder = StringBuilder()
        digest.indices.forEach { i ->
            val b = (0xFF and digest[i].toInt())
            if (b < 16)
                stringBuilder.append("0")
            stringBuilder.append(Integer.toHexString(b))
        }
        return stringBuilder.toString()
    }

    private fun generateScrobblingApiSig(sessionKey: String, track: Track): String {
        var str = "album[0]${track.albumName}"
        str += "api_key${appSettings.apiKey}"
        str += "artist[0]${track.artistName}"
        str += "method${scrobbleMethod}"
        str += "sk${sessionKey}"
        str += "timestamp[0]${track.timeStamp}"
        str += "track[0]${track.name}"
        str += appSettings.sharedSecret

        val md = MessageDigest.getInstance("MD5")
        val data = str.toByteArray()
        md.update(data)
        val digest = md.digest()
        val stringBuilder = StringBuilder()
        digest.indices.forEach { i ->
            val b = (0xFF and digest[i].toInt())
            if (b < 16)
                stringBuilder.append("0")
            stringBuilder.append(Integer.toHexString(b))
        }
        return stringBuilder.toString()
    }

    private fun createTrack(intent: Intent): Track {
        val artistName = intent.getStringExtra("artistName")
        val trackName = intent.getStringExtra("trackName")
        val albumName = intent.getStringExtra("albumName")
        val playingTime = intent.getLongExtra("playingTime", appSettings.defaultPlayingTime)
        val timeStamp = intent.getLongExtra("timeStamp", System.currentTimeMillis() / 1000L)
        val albumArtWork = getAlbumInfo(albumName, artistName, trackName)
        return Track(artistName, trackName, albumName, playingTime, timeStamp, albumArtWork)
    }

    private fun overScrobblingPoint(timeStamp: Long, playingTime: Long): Boolean {
        val now = System.currentTimeMillis() / 1000L
        val scrobblingPoint = playingTime / 2
        return (now - timeStamp) > scrobblingPoint
    }
}