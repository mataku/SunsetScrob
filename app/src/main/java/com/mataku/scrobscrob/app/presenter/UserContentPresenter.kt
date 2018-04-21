package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.api.service.UserTopAlbumsService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserContentPresenter {
    fun getTopAlbums(userName: String) {

        Retrofit2LastFmClient.create(UserTopAlbumsService::class.java)
                .getTopAlbum(userName, "overall")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {

                    }
                })
    }
}