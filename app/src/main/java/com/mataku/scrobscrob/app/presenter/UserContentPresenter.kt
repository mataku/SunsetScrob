package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.api.LastFmApiClient
import com.mataku.scrobscrob.app.model.api.service.UserTopAlbumsService
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class UserContentPresenter {

    private val job = Job()

    fun getTopAlbums(userName: String) {
        launch(job + UI) {
            requestTopAlbums(userName)
        }
    }

    private suspend fun requestTopAlbums(userName: String) {

        val result = LastFmApiClient.create(UserTopAlbumsService::class.java)
                .getTopAlbum(userName, "overall").await()
    }
}