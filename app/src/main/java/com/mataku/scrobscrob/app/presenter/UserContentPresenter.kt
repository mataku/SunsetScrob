package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.api.LastFmApiClient
import com.mataku.scrobscrob.app.model.api.service.UserTopAlbumsService
import com.mataku.scrobscrob.app.util.AppUtil
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class UserContentPresenter {

    private val job = Job()

    private val appUtil = AppUtil()

    fun getTopAlbums(userName: String, page: Int) {
        launch(job + UI) {
            requestTopAlbums(userName, page)
        }
    }

    private suspend fun requestTopAlbums(userName: String, page: Int) {

        val result = LastFmApiClient.create(UserTopAlbumsService::class.java)
                .getTopAlbum(appUtil.topAlbumsCountPerPage, page, "overall", userName).await()
    }
}