package com.mataku.scrobscrob.app.ui.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mataku.scrobscrob.app.data.repository.TopAlbumsRepository
import com.mataku.scrobscrob.app.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.app.model.entity.Artist
import com.mataku.scrobscrob.app.model.entity.presentation.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TopViewModel(
    private val topAlbumRepository: TopAlbumsRepository,
    private val topArtistsRepository: TopArtistsRepository
) : ViewModel(), CoroutineScope {

    val topAlbumsResult = MutableLiveData<Result<List<Album>>>()
    val topArtistsResult = MutableLiveData<Result<List<Artist>>>()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun loadAlbums(page: Int, userName: String) {
        launch(coroutineContext) {
            topAlbumsResult.postValue(topAlbumRepository.topAlbumsResponse(page, userName))
        }
    }

    fun loadArtists(page: Int, userName: String) {
        launch(coroutineContext) {
            topArtistsResult.postValue(topArtistsRepository.topArtistsResponse(page, userName))
        }

    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}