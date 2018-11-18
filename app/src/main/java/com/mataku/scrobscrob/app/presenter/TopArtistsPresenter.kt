package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.api.LastFmApiClient
import com.mataku.scrobscrob.app.model.api.service.UserTopArtistsService
import com.mataku.scrobscrob.app.ui.view.TopArtistsContentViewCallback
import com.mataku.scrobscrob.app.util.AppUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TopArtistsPresenter(var view: TopArtistsContentViewCallback) {

    private val appUtil = AppUtil()

    private val job = Job()

    private val coroutineContext = job + Dispatchers.Main

    fun getTopArtists(userName: String, page: Int) {
        CoroutineScope(coroutineContext).launch {
            requestTopArtists(userName, page)
        }
    }

    private suspend fun requestTopArtists(userName: String, page: Int) {
        val result = LastFmApiClient.create(UserTopArtistsService::class.java)
            .getTopArtists(appUtil.topAlbumsCountPerPage, page, "overall", userName).await()
        when (result.code()) {
            200 -> {
                result.body()?.topArtists.let {
                    it?.artists.let { artistList ->
                        view.show(artistList!!)
                    }
                }
            }
        }
    }
}