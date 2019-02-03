package com.mataku.scrobscrob.app.ui.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mataku.scrobscrob.app.model.api.LastFmApiClient
import com.mataku.scrobscrob.app.model.api.service.UserTopAlbumsService
import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.app.model.entity.presentation.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TopViewModel : ViewModel(),
    CoroutineScope {

    val topAlbumsResult = MutableLiveData<Result<List<Album>>>()

    private val job = Job()
    private var isLastAlbum = false
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun loadAlbums(page: Int, userName: String) {
        if (isLastAlbum) {
            return
        }

        launch(coroutineContext) {
            // TODO: repository
            val result = LastFmApiClient.create(UserTopAlbumsService::class.java)
                .getTopAlbum(20, page, "overall", userName).await()
            when (result.code()) {
                200 -> {
                    result.body()?.topAlbums?.let {
                        if (it.albums.isEmpty()) {
                            isLastAlbum = true
                        } else {
                            topAlbumsResult.postValue(Result.success(it.albums))
                        }
                    }
                }
            }
        }
    }

    fun loadArtists() {

    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}