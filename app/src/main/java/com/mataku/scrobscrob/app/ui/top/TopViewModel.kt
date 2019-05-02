package com.mataku.scrobscrob.app.ui.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mataku.scrobscrob.app.data.repository.TopAlbumsRepository
import com.mataku.scrobscrob.app.data.repository.TopArtistsRepository
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.core.api.endpoint.Artist
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TopViewModel(
    private val topAlbumRepository: TopAlbumsRepository,
    private val topArtistsRepository: TopArtistsRepository
) : ViewModel(), CoroutineScope {

    val topAlbumsResult = MutableLiveData<SunsetResult<List<Album>>>()
    val topArtistsResult = MutableLiveData<SunsetResult<List<Artist>>>()

    private val job = Job()
    override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main

    fun loadAlbums(page: Int, userName: String) {
        launch(coroutineContext) {
            val result = async {
                topAlbumRepository.topAlbumsResponse(page, userName)
            }
            topAlbumsResult.postValue(result.await())
        }
    }

    fun loadArtists(page: Int, userName: String) {
        launch(coroutineContext) {
            val result = async {
                topArtistsRepository.topArtistsResponse(page, userName)
            }
            topArtistsResult.postValue(result.await())
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}