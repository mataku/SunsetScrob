package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.api.LastFmApiClient
import com.mataku.scrobscrob.app.model.api.service.AuthMobileSessionService
import com.mataku.scrobscrob.app.ui.view.LoginViewCallback
import com.mataku.scrobscrob.app.util.AppUtil
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class LoginPresenter(private var accessible: Boolean, var view: LoginViewCallback) {
    private val appUtil = AppUtil()
    private val method = "auth.getMobileSession"

    private val job = Job()

    fun auth(userName: String, password: String) {
        launch(job + UI) {
            authenticate(userName, password)
        }
    }

    fun dispose() {
        job.cancel()
    }

    private suspend fun authenticate(userName: String, password: String) {
        val params: MutableMap<String, String> = mutableMapOf()
        params["username"] = userName
        params["password"] = password
        params["method"] = method

        val apiSig: String = appUtil.generateApiSig(params)
        val client = LastFmApiClient.create(AuthMobileSessionService::class.java)


        val result = client.auth(userName, password, apiSig).await()
        when (result.code()) {
            200, 201 -> {
                val mobileSession = result.body()?.mobileSession
                mobileSession?.let {
                    view.setSessionInfo(it.key, it.name)
                }
            }
            else -> {
                view.showError()
            }
        }
    }

    fun backToSettingsWhenLoggedIn(success: Boolean?, sessionKey: String) {
        if (success!! && sessionKey.isNotEmpty()) {
            if (accessible) {
                view.showSuccessMessage()
            } else {
                view.showSuccessMessage()
                view.showMessageToAllowAccessToNotification()
            }
            view.backToSettingsActivity()
        } else {
            view.focusOnPasswordView()
        }
    }
}