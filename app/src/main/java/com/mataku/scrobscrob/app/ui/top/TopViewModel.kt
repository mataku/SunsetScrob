package com.mataku.scrobscrob.app.ui.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mataku.scrobscrob.core.api.endpoint.Album
import com.mataku.scrobscrob.core.api.endpoint.Artist
import com.mataku.scrobscrob.core.api.repository.TopAlbumsRepository
import com.mataku.scrobscrob.core.api.repository.TopArtistsRepository
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
            topAlbumRepository.topAlbumsResponse(page, userName)
                .onEach {
                    topAlbumsResult.postValue(it)
                }
                .flowOn(Dispatchers.IO)
                .launchIn(CoroutineScope(coroutineContext))
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